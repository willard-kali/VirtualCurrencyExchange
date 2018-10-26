package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.model.Response;
import com.business.exchange.model.TaskResponse;

public interface TaskService {

    Response initiate(int publisherID, String taskName, int bounty);

    Response finish(int taskId, String employeeID);

    TaskResponse queryAll();
}
