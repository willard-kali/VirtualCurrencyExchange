package com.business.exchange.service;

import com.business.exchange.model.TaskResponse;
import com.business.exchange.model.TasksResponse;

public interface TaskService {

    TaskResponse create(String employeeID, String taskName, int bounty);

    TaskResponse close(int taskId, String employeeID);

    TasksResponse queryAll(int currentPage, int pageSize, String expression);

    TasksResponse queryMine(int currentPage, int pageSize, String employeeID, String expression);

    boolean receive(String employeeID, int taskId);
}
