package com.lhq.aianswer.scoring;

import com.lhq.aianswer.common.ErrorCode;
import com.lhq.aianswer.exception.BusinessException;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.UserAnswer;
import com.lhq.aianswer.model.enums.AppTypeEnum;
import com.lhq.aianswer.model.enums.ScoringStategyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评分策略上下文
 *
 * @author lhq
 * @version 1.0
 * @date 2025/5/7 下午11:18
 */
@Service
@Deprecated
public class ScoringStrategyContext {
    
    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;
    
    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;
    
    /**
     * 根据应用类型选择评分策略
     *
     * @param chioceList
     * @param app
     * @return
     */
    public UserAnswer doScore(List<String> chioceList, App app) {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        ScoringStategyEnum scoringStategyEnum = ScoringStategyEnum.getEnumByValue(app.getScoringStrategy());
        if (appTypeEnum == null || scoringStategyEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用类型或评分策略不存在");
        }
        switch (appTypeEnum) {
            case SCORE:
                switch (scoringStategyEnum) {
                    case CUSTOM:
                        return customScoreScoringStrategy.doScore(chioceList, app);
                    case AI:
                        break;
                }
            case TEST:
                switch (scoringStategyEnum) {
                    case CUSTOM:
                        return customTestScoringStrategy.doScore(chioceList, app);
                    case AI:
                        break;
                }
                
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        
    }
}
