package com.lhq.aianswer.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lhq.aianswer.model.dto.question.QuestionContentDTO;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.Question;
import com.lhq.aianswer.model.entity.ScoringResult;
import com.lhq.aianswer.model.entity.UserAnswer;
import com.lhq.aianswer.model.enums.ScoringStategyEnum;
import com.lhq.aianswer.model.vo.QuestionVO;
import com.lhq.aianswer.model.vo.ScoringResultVO;
import com.lhq.aianswer.service.QuestionService;
import com.lhq.aianswer.service.ScoringResultService;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义测评类应用评分策略
 * @author lhq
 * @version 1.0
 * @date 2025/4/29 上午10:12
 */
@Service
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy{
    
    @Resource
    private QuestionService questionService;
    
    @Resource
    private ScoringResultService scoringResultService;
    
    
    @Override
    public UserAnswer doScore(List<String> choiceList, App app) {
        
        // 初始化一个对象，用于存储每个选项的计数
        Map<String, Integer> optionCount = new HashMap<>();
        
        Long appId = app.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class)
            .eq(Question::getAppId, appId));
        
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class)
                .eq(ScoringResult::getAppId, appId));
        // 获取题目内容
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContentList = questionVO.getQuestionContent();
       
        // 遍历答案列表和题目列表
        for (int i = 0; i < choiceList.size(); i++) {
            QuestionContentDTO questionContentDTO = questionContentList.get(i);
            String answer = choiceList.get(i);
            // 遍历题目中的选项
            for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                // 如果答案和选项的key匹配
                if (option.getKey().equals(answer)) {
                    String result = option.getResult();
                    // 如果result属性不在optionCount中，初始化为0
                    optionCount.putIfAbsent(result, 0);
                    // 将对应的result计数加1
                    optionCount.put(result, optionCount.get(result) + 1);
                }
            }
        }
        
        //初始化最高分数和结果
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);
        // 遍历评分结果列表
        for (ScoringResult result : scoringResultList) {
            // 获取当前评分结果的计数
            List<String> resultProp = JSONUtil.toList(result.getResultProp(), String.class);
            int score = resultProp.stream()
                .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                .sum();
            
            // 如果当前评分结果的计数大于最高分数，更新最高分数和对应的评分结果
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = result;
            }
        }
        
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(ScoringStategyEnum.CUSTOM.getValue());
        userAnswer.setChoices(JSONUtil.toJsonStr(choiceList));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        return userAnswer;
    }
}


