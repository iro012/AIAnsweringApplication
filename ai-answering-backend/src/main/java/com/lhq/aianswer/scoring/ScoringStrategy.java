package com.lhq.aianswer.scoring;

import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 * @author lhq
 * @version 1.0
 * @date 2025/4/29 上午10:02
 */
public interface ScoringStrategy {
    
    /**
     * 执行评分
     * @param choiceList
     * @param app
     * @return
     */
    UserAnswer doScore(List<String> choiceList, App app);
}
