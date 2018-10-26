package com.business.exchange.model;

public enum TaskStatus {

    ONGOING("ongoing"),
    CLOSED("closed");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
