package com.business.exchange.controller;

import com.alibaba.fastjson.JSON;
import com.business.exchange.constant.RespDefine;
import com.business.exchange.constant.UserConstants;
import com.business.exchange.domain.Task;
import com.business.exchange.model.TaskResponse;
import com.business.exchange.model.TasksResponse;
import com.business.exchange.service.TaskService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public TaskResponse create(@RequestBody(required = true) Task task, HttpSession session) {
        TaskResponse createResponse = new TaskResponse(RespDefine.ERR_CODE_TASK_CREATE_FAILED, RespDefine.ERR_DESC_TASK_CREATE_FAILED);
        if (null == task || null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("param invalid.");
            return createResponse;
        }

//        int publisherID = task.getPublisherID();
        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();
        String taskName = task.getTaskName();
        int bounty = task.getBounty();

        if (employeeID.length() > UserConstants.EMPLOYEE_ID_MAX_LENGTH
                || null == taskName || taskName.isEmpty()) {
            LOGGER.error("param invalid.");
            return createResponse;
        }

        if (bounty <= 0 || !CurrencyUtils.isValidCurrencyNumber(bounty)) {
            LOGGER.error("bounty is invalid.");
            createResponse = new TaskResponse(RespDefine.ERR_CODE_EXCHANGE_CURRENCY_IS_INVALID,
                    RespDefine.ERR_DESC_EXCHANGE_CURRENCY_IS_INVALID);
            return createResponse;
        }

        createResponse = taskService.create(employeeID, taskName, bounty);
        LOGGER.info("initiate task {}.", taskName);
        return createResponse;
    }

    /**
     * 关闭任务
     * @param taskId
     * @return
     */
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public TaskResponse close(@RequestBody(required = true) String taskId, HttpSession session) {
        LOGGER.info("input param: {}.", taskId);
        int taskIdNum = JSON.parseObject(taskId).getInteger("taskId");
        TaskResponse finishResponse = new TaskResponse(RespDefine.ERR_CODE_TASK_FINISH_FAILED, RespDefine.ERR_DESC_TASK_FINISH_FAILED);
        if (taskIdNum <= 0 || null == session) {
            LOGGER.error("task id invalid.");
            return finishResponse;
        }
        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        if (null == employeeID || employeeID.isEmpty()) {
            LOGGER.error("session invalid.");
            return finishResponse;
        }

        finishResponse = taskService.close(taskIdNum, employeeID);
        LOGGER.info("close task {}.", taskId);
        return finishResponse;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public TasksResponse queryAll() {
        LOGGER.info("query all task.");
        return taskService.queryAll();
    }

    @RequestMapping(value = "mine", method = RequestMethod.GET)
    public TasksResponse queryMine(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "expression", defaultValue = "") String expression,
                                   HttpSession session) {
        TasksResponse myTaskResp = new TasksResponse(RespDefine.ERR_CODE_TASK_QUERY_FAILED,
                RespDefine.ERR_DESC_TASK_QUERY_FAILED);

        if (null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session error.");
            return myTaskResp;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        myTaskResp = taskService.queryMine(currentPage, pageSize, employeeID, expression);
        LOGGER.info("query mine({}) task.", employeeID);
        return myTaskResp;
    }

    @RequestMapping(value = "receive", method = RequestMethod.GET)
    public boolean receive(Integer taskId, HttpSession session) {
        if (null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session error.");
            return false;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();
        return taskService.receive(employeeID, taskId);
    }
}
