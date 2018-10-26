package com.business.exchange.model;

import com.business.exchange.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskResponse extends Response {

    private List<Task> tasks = new ArrayList<Task>();

    public TaskResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public TaskResponse(int resultCode, String errDesc, List<Task> tasks) {
        super(resultCode, errDesc);
        this.tasks = tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
