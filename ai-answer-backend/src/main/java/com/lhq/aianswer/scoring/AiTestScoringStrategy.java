package com.lhq.aianswer.scoring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lhq.aianswer.manager.DeepSeekManager;
import com.lhq.aianswer.model.dto.question.QuestionContentDTO;
import com.lhq.aianswer.model.dto.user_answer.QuestionAnswerDTO;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.Question;
import com.lhq.aianswer.model.entity.ScoringResult;
import com.lhq.aianswer.model.entity.UserAnswer;
import com.lhq.aianswer.model.enums.ScoringStategyEnum;
import com.lhq.aianswer.model.vo.QuestionVO;
import com.lhq.aianswer.service.QuestionService;
import com.lhq.aianswer.service.ScoringResultService;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义测评类应用评分策略
 * @author lhq
 * @version 1.0
 * @date 2025/4/29 上午10:12
 */
@Service
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
@Slf4j
public class AiTestScoringStrategy implements ScoringStrategy{
    
    @Resource
    private QuestionService questionService;
    
    @Resource
    private ScoringResultService scoringResultService;
    
    @Resource
    private DeepSeekManager deepSeekManager;
    
    @Resource
    private RedissonClient redissonClient;
    
    
    private final Cache<String, String> answerCacheMap = Caffeine.newBuilder().initialCapacity(1024)
        .expireAfterAccess(5L, TimeUnit.MINUTES)
        .build();
    
    private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";
    
    private static final String AI_TEST_SCORING_SYSTEM_MESSAGE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
        "```\n" +
        "应用名称，\n" +
        "【【【应用描述】】】，\n" +
        "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
        "```\n" +
        "\n" +
        "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
        "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
        "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
        "```\n" +
        "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
        "```\n" +
        "3. 返回格式必须为 JSON 对象";
    
    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
        return userMessage.toString();
    }
    
    
    @Override
    public UserAnswer doScore(List<String> choiceList, App app) {
        
        // 初始化一个对象，用于存储每个选项的计数
        Map<String, Integer> optionCount = new HashMap<>();
        
        Long appId = app.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class)
            .eq(Question::getAppId, appId));
        
        // 获取题目内容
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContentList = questionVO.getQuestionContent();
        // 获取缓存key
        String jsonStr = JSONUtil.toJsonStr(choiceList);
        String cacheKey = buildCacheKey(appId, jsonStr);
        String answerJson = answerCacheMap.getIfPresent(cacheKey);
        // 如果缓存中存在，直接返回缓存中的结果
        if (StrUtil.isNotBlank(answerJson)) {
            UserAnswer userAnswer = JSONUtil.toBean(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(jsonStr);
            return userAnswer;
        }
        RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + cacheKey);
        try {
            // 竞争分布式锁
            boolean res = lock.tryLock(3, 15, TimeUnit.SECONDS);
            if (!res) {
                return null;
            }
            // 调用ai获取结果
            // 封装prompt
            String userMessage = getAiTestScoringUserMessage(app, questionContentList, choiceList);
            
            
            // 调用ai
            String result = deepSeekManager.doSyncStableRequest(AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            String json = result.substring(start, end + 1);
            
            // 缓存结果
            answerCacheMap.put(cacheKey, json);
            
            // 结果处理
            UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(ScoringStategyEnum.CUSTOM.getValue());
            userAnswer.setChoices(JSONUtil.toJsonStr(choiceList));
            return userAnswer;
        }catch (Exception e) {
            log.error("调用ai失败", e);
        }
        finally {
            if (lock != null && lock.isLocked()) {
             if (lock.isHeldByCurrentThread()){
                 lock.unlock();
             }
            }
        }
        return null;
    }
    
    /**
     * 获取缓存key
     * @param appId
     * @param choicesStr
     * @return
     */
    private String buildCacheKey(Long appId, String choicesStr) {
        return DigestUtil.md5Hex(appId + ":" + choicesStr);
    }
}


