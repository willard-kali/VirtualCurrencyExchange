package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.constant.UserConstants;
import com.business.exchange.domain.Task;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.TaskResponse;
import com.business.exchange.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody(required = true) Task task, HttpSession session) {
        BaseResponse createResponse = new BaseResponse(RespDefine.ERR_CODE_TASK_CREATE_FAILED, RespDefine.ERR_DESC_TASK_CREATE_FAILED);
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

        if (employeeID.length() > UserConstants.EMPLOYEE_ID_MAX_LENGTH || null == taskName || taskName.isEmpty() || bounty <= 0) {
            LOGGER.error("param invalid.");
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
    public BaseResponse close(@RequestBody(required = true) int taskId, HttpSession session) {
        BaseResponse finishResponse = new BaseResponse(RespDefine.ERR_CODE_TASK_FINISH_FAILED, RespDefine.ERR_DESC_TASK_FINISH_FAILED);
        if (taskId <= 0 || null == session) {
            LOGGER.error("task id invalid.");
            return finishResponse;
        }
        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        if (null == employeeID || employeeID.isEmpty()) {
            LOGGER.error("session invalid.");
            return finishResponse;
        }

        finishResponse = taskService.close(taskId, employeeID);
        LOGGER.info("close task {}.", taskId);
        return finishResponse;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public TaskResponse queryAll() {
        LOGGER.info("query all task.");
        return taskService.queryAll();
    }

    @RequestMapping(value = "mine", method = RequestMethod.GET)
    public TaskResponse queryMine(HttpSession session) {
        TaskResponse myTaskResp = new TaskResponse(RespDefine.ERR_CODE_TASK_QUERY_FAILED,
                RespDefine.ERR_DESC_TASK_QUERY_FAILED);

        if (null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session error.");
            return myTaskResp;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        myTaskResp = taskService.queryMine(employeeID);
        LOGGER.info("query mine({}) task.", employeeID);
        return myTaskResp;
    }
}
