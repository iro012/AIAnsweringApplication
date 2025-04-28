package com.lhq.aianswer.model.enums;

import lombok.Getter;

/**
 * 应用类型枚举
 * @author lhq
 * @version 1.0
 * @date 2025/4/25 上午10:00
 */
@Getter
public enum AppTypeEnum {
    
    SCORE("得分类", 0),
    TEST("测评类", 1);
    
    private final String text;
    
    private final Integer value;
    
    AppTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }
    
    /**
     * 根据 value 获取 emun
     * @param value
     * @return
     */
    public static AppTypeEnum getEnumByValue(Integer value) {
        for (AppTypeEnum appTypeEnum : AppTypeEnum.values()) {
            if (appTypeEnum.getValue() == value) {
                return appTypeEnum;
            }
        }
        return null;
    }
}

