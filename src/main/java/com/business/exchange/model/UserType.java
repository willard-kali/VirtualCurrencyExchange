package com.business.exchange.model;

/**
 * 用户类型
 */
public enum UserType {
    //普通用户
    ORDINARY_USER("user"),
    //管理员
    ADMIN_USER("admin");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
