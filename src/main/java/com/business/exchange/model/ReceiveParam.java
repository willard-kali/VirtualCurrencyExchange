package com.business.exchange.model;

public class ReceiveParam {

    private int taskId;

    public ReceiveParam(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
