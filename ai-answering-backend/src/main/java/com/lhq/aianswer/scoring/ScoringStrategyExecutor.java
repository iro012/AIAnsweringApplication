package com.lhq.aianswer.scoring;

import com.lhq.aianswer.common.ErrorCode;
import com.lhq.aianswer.exception.BusinessException;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/7 下午11:37
 */
@Service
public class ScoringStrategyExecutor {
    @Resource
    private List<ScoringStrategy> scoringStrategyList;
    
    /**
     * 根据应用类型选择评分策略
     * @param choiceList
     * @param app
     * @return
     */
    public UserAnswer doScore(List<String> choiceList, App app) {
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        
        if (appType == null && scoringStrategy == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用类型和评分策略不能为空");
        }
        
        for (ScoringStrategy strategy : scoringStrategyList) {
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                if (scoringStrategyConfig.appType() == appType && scoringStrategyConfig.scoringStrategy() == scoringStrategy) {
                    return strategy.doScore(choiceList, app);
                }
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的评分策略");
    }
}
