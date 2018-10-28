package com.business.exchange.service;

import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.TaskResponse;

public interface TaskService {

    BaseResponse create(String employeeID, String taskName, int bounty);

    BaseResponse close(int taskId, String employeeID);

    TaskResponse queryAll();

    TaskResponse queryMine(String employeeID);
}
