package com.business.exchange.model;

import com.business.exchange.domain.Task;

public class TaskResponse extends BaseResponse {
    private Task task = new Task();

    public TaskResponse() {
    }

    public TaskResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public TaskResponse(int resultCode, String errDesc, Task task) {
        super(resultCode, errDesc);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
