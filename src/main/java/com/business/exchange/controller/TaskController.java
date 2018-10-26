package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Task;
import com.business.exchange.model.Response;
import com.business.exchange.model.TaskResponse;
import com.business.exchange.service.TaskService;
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

    @RequestMapping(value = "initiate", method = RequestMethod.POST)
    public Response initiate(@RequestBody Task task, HttpSession session) {
        Response taskResp = new Response(RespDefine.ERR_CODE_TASK_CREATE_FAILED, RespDefine.ERR_DESC_TASK_CREATE_FAILED);
        if (null == task || null == session) {
            LOGGER.error("param invalid.");
            return taskResp;
        }

        int publisherID = task.getPublisherID();
        String taskName = task.getTaskName();
        int bounty = task.getBounty();

        if (publisherID < 0 || null == taskName || taskName.isEmpty() || bounty <= 0) {
            LOGGER.error("param invalid.");
            return taskResp;
        }

        taskResp = taskService.initiate(publisherID, taskName, bounty);

        return taskResp;
    }

    /**
     * 关闭任务
     * @param taskId
     * @return
     */
    @RequestMapping(value = "finish", method = RequestMethod.GET)
    public Response finish(@RequestParam(value = "taskId", required = true) int taskId, HttpSession httpSession) {
        Response finishResponse = new Response(RespDefine.ERR_CODE_TASK_FINISH_FAILED, RespDefine.ERR_DESC_TASK_FINISH_FAILED);
        if (taskId <= 0 || null == httpSession) {
            LOGGER.error("task id invalid.");
            return finishResponse;
        }
        String employeeID = httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        if (null == employeeID || employeeID.isEmpty()) {
            LOGGER.error("session invalid.");
            return finishResponse;
        }

        finishResponse = taskService.finish(taskId, employeeID);

        return finishResponse;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public TaskResponse queryAll() {
        return taskService.queryAll();
    }
}
