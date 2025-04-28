package com.lhq.aianswer.model.enums;

import lombok.Getter;

/**
 * @author lhq
 * @version 1.0
 * @date 2025/4/25 上午10:00
 */
@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("被封号", "ban");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    
    /**
     * 根据 value 获取 emun
     * @param value
     * @return
     */
    public static UserRoleEnum getEnumByValue(String value) {
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.getValue().equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
