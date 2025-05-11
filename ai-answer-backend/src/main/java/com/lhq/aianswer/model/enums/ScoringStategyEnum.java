package com.lhq.aianswer.model.enums;

import lombok.Getter;

/**
 * 计分策略枚举
 * @author lhq
 * @version 1.0
 * @date 2025/4/25 上午10:00
 */
@Getter
public enum ScoringStategyEnum {
    
    CUSTOM("自定义", 0),
    AI("AI", 1);
    
    private final String text;
    
    private final Integer value;
    
    ScoringStategyEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }
    
    /**
     * 根据 value 获取 emun
     * @param value
     * @return
     */
    public static ScoringStategyEnum getEnumByValue(Integer value) {
        for (ScoringStategyEnum scoringStategyEnum : ScoringStategyEnum.values()) {
            if (scoringStategyEnum.getValue() == value) {
                return scoringStategyEnum;
            }
        }
        return null;
    }
}

