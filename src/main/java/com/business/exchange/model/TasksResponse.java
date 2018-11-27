package com.business.exchange.model;

import com.business.exchange.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksResponse extends BaseResponse {

    private List<Task> tasks = new ArrayList<Task>();

    public TasksResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public TasksResponse(int resultCode, String errDesc, List<Task> tasks) {
        super(resultCode, errDesc);
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
