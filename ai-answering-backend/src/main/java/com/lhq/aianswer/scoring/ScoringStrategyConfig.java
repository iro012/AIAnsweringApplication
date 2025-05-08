package com.lhq.aianswer.scoring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/5/7 下午11:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {
    /**
     * 应用类型
     * @return
     */
    int appType();
    
    /**
     * 评分策略
     * @return
     */
    int scoringStrategy();
}
