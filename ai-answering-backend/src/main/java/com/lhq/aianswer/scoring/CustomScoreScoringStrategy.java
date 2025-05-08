package com.lhq.aianswer.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lhq.aianswer.common.ErrorCode;
import com.lhq.aianswer.exception.BusinessException;
import com.lhq.aianswer.model.dto.question.QuestionContentDTO;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.Question;
import com.lhq.aianswer.model.entity.ScoringResult;
import com.lhq.aianswer.model.entity.UserAnswer;
import com.lhq.aianswer.model.enums.ScoringStategyEnum;
import com.lhq.aianswer.model.vo.QuestionVO;
import com.lhq.aianswer.service.QuestionService;
import com.lhq.aianswer.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义打分类应用评分策略
 * @author lhq
 * @version 1.0
 * @date 2025/5/7 下午10:40
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {
    
    @Resource
    private QuestionService questionService;
    
    @Resource
    private ScoringResultService scoringResultService;
    
    @Override
    public UserAnswer doScore(List<String> choiceList, App app) {
        // 根据id查询题目和题目结果信息
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class)
            .eq(Question::getAppId, app.getId()));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class)
            .eq(ScoringResult::getAppId, app.getId()));
        
        // 统计用户总分数
        int totalScore = 0;
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        // 校验数量
        if (choiceList.size() != questionContent.size()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目和用户答案数量不一致");
        }
        // 遍历题目列表
        for (int i = 0; i < questionContent.size(); i++) {
            Map<String, Integer> resultMap = questionContent.get(i).getOptions().stream()
                .collect(Collectors.toMap(QuestionContentDTO.Option::getKey, QuestionContentDTO.Option::getScore));
            Integer score = Optional.ofNullable(resultMap.get(choiceList.get(i))).orElse(0);
            totalScore += score;
        }
        // 根据分数范围匹配结果
        ScoringResult maxScoringResult = scoringResultList.get(0);
        for (ScoringResult result : scoringResultList) {
            if (totalScore >= result.getResultScoreRange()) {
                maxScoringResult = result;
                break;
            }
        }
        // 封装用户答案
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(ScoringStategyEnum.CUSTOM.getValue());
        userAnswer.setChoices(JSONUtil.toJsonStr(choiceList));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);
        return userAnswer;
    }
}
