package com.lhq.aianswer.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhq.aianswer.annotation.AuthCheck;
import com.lhq.aianswer.common.BaseResponse;
import com.lhq.aianswer.common.DeleteRequest;
import com.lhq.aianswer.common.ErrorCode;
import com.lhq.aianswer.common.ResultUtils;
import com.lhq.aianswer.constant.UserConstant;
import com.lhq.aianswer.exception.BusinessException;
import com.lhq.aianswer.exception.ThrowUtils;
import com.lhq.aianswer.manager.DeepSeekManager;
import com.lhq.aianswer.model.dto.question.*;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.Question;
import com.lhq.aianswer.model.enums.AppTypeEnum;
import com.lhq.aianswer.model.vo.QuestionVO;
import com.lhq.aianswer.model.vo.UserVO;
import com.lhq.aianswer.service.AppService;
import com.lhq.aianswer.service.QuestionService;
import com.lhq.aianswer.service.UserService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 问题接口
 *
 * @author lhq
 * @version 1.0
 * @date 25-04-2025 00:11:06
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {
    
    @Resource
    private QuestionService questionService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private AppService appService;
    
    @Resource
    private DeepSeekManager deepSeekManager;
    
    @Resource
    private Scheduler vipScheduler;
    
    /**
     * 创建问题
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        //在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        question.setQuestionContent(JSONUtil.toJsonStr(questionAddRequest.getQuestionContent()));
        // 数据校验
        questionService.validQuestion(question, true);
        // 填充默认值
        UserVO loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }
    
    /**
     * 删除问题
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    
    /**
     * 更新问题（仅管理员可用）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        question.setQuestionContent(JSONUtil.toJsonStr(questionUpdateRequest.getQuestionContent()));
        // 数据校验
        questionService.validQuestion(question, false);
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    
    /**
     * 根据 id 获取问题（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }
    
    /**
     * 分页获取问题列表（仅管理员可用）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
            questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }
    
    /**
     * 分页获取问题列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
            questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }
    
    /**
     * 分页获取当前登录用户创建的问题列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        UserVO loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
            questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }
    
    /**
     * 编辑问题（给用户使用）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        question.setQuestionContent(JSONUtil.toJsonStr(questionEditRequest.getQuestionContent()));
        // 数据校验
        questionService.validQuestion(question, false);
        UserVO loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    
    
    // region AI生成问题
    // 1. 模板定义
    private static final String GENERATE_QUESTION_SYSTEM_MESSAGE = "你是一位严谨的出题专家，我会给你如下信息：\n" +
        "```\n" +
        "应用名称，\n" +
        "【【【应用描述】】】，\n" +
        "应用类别，\n" +
        "要生成的题目数，\n" +
        "每个题目的选项数\n" +
        "```\n" +
        "\n" +
        "请你根据上述信息，按照以下步骤来出题：\n" +
        "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" +
        "2. 严格按照下面的 json 格式输出题目和选项\n" +
        "```\n" +
        "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" +
        "```\n" +
        "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
        "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
        "4. 返回的题目列表格式必须为 JSON 数组";
    
    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        userMessage.append(AppTypeEnum.getEnumByValue(app.getAppType()).getText()).append("\n");
        userMessage.append(questionNumber).append("\n");
        userMessage.append(optionNumber);
        return userMessage.toString();
    }
    
    
    @PostMapping("/ai_generate")
    public BaseResponse<List<QuestionContentDTO>> aiGenerateQuestion(@RequestBody AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        String result = deepSeekManager.doSyncUnstableRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
        // 结果处理
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(json, QuestionContentDTO.class);
        return ResultUtils.success(questionContentDTOList);
    }
    // endregion
    
    @GetMapping("/ai_generate/sse")
    public SseEmitter aiGenerateQuestionSse(AiGenerateQuestionRequest aiGenerateQuestionRequest, HttpServletRequest request) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        Flowable<ChatCompletionChunk> chatCompletionChunkFlowable = deepSeekManager.doStreamRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage, null);
        StringBuilder contentBuilder = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        // 默认线程池
        Scheduler scheduler = Schedulers.single();
        UserVO loginUser = userService.getLoginUser(request);
        if ("vip".equals(loginUser.getUserRole())) {
            scheduler = vipScheduler;
        }
        //异步线程执行
        chatCompletionChunkFlowable.observeOn(scheduler)
            .map(chuck -> chuck.getChoices().get(0).getMessage().getContent().toString())
            .map(message -> message.replaceAll("\\s", ""))
            .filter(StrUtil::isNotBlank)
            .flatMap(message -> {
                ArrayList<Character> charList = new ArrayList<>();
                for (char c : message.toCharArray()) {
                    charList.add(c);
                }
                return Flowable.fromIterable(charList);
            })
            .doOnNext(c -> {
                // 识别第一个 { 表示开始 AI 传输 json 数据，打开 counter 开始拼接 json 数据
                if (c == '{') {
                    counter.addAndGet(1);
                }
                
                if (counter.get() > 0) {
                    contentBuilder.append(c);
                }
                
                if (c == '}') {
                    counter.addAndGet(-1);
                    if (counter.get() == 0) {
                        sseEmitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
                        contentBuilder.setLength(0);
                    }
                }
            }).doOnComplete(sseEmitter::complete).subscribe();
        
        return sseEmitter;
    }
    
    @GetMapping("/ai_generate/sse/test")
    public SseEmitter aiGenerateQuestionSseTest(AiGenerateQuestionRequest aiGenerateQuestionRequest, Boolean isVip) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 封装 Prompt
        String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
        // AI 生成
        Flowable<ChatCompletionChunk> chatCompletionChunkFlowable = deepSeekManager.doStreamRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage, null);
        StringBuilder contentBuilder = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        // 默认线程池
        Scheduler scheduler = Schedulers.single();
        if (isVip) {
            scheduler = vipScheduler;
        }
        //异步线程执行
        chatCompletionChunkFlowable.observeOn(scheduler)
            .map(chuck -> chuck.getChoices().get(0).getMessage().getContent().toString())
            .map(message -> message.replaceAll("\\s", ""))
            .filter(StrUtil::isNotBlank)
            .flatMap(message -> {
                ArrayList<Character> charList = new ArrayList<>();
                for (char c : message.toCharArray()) {
                    charList.add(c);
                }
                return Flowable.fromIterable(charList);
            })
            .doOnNext(c -> {
                // 识别第一个 { 表示开始 AI 传输 json 数据，打开 counter 开始拼接 json 数据
                if (c == '{') {
                    counter.addAndGet(1);
                }
                
                if (counter.get() > 0) {
                    contentBuilder.append(c);
                }
                
                if (c == '}') {
                    counter.addAndGet(-1);
                    if (counter.get() == 0) {
                        System.out.println(Thread.currentThread().getName());
                        if (!isVip) {
                            Thread.sleep(10000L);
                        }
                        sseEmitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
                        contentBuilder.setLength(0);
                    }
                }
            }).doOnComplete(sseEmitter::complete).subscribe();
        
        return sseEmitter;
    }
}
