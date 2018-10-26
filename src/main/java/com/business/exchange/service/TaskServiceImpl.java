package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Task;
import com.business.exchange.domain.TaskRepository;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserRepository;
import com.business.exchange.model.Response;
import com.business.exchange.model.TaskResponse;
import com.business.exchange.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final String PUBLISH_TIME_NAME = "publishTime";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发布任务
     * @param publisherID 发布者ID
     * @param taskName 任务名称
     * @param bounty 悬赏金额
     * @return 发布状态
     */
    @Override
    public Response initiate(int publisherID, String taskName, int bounty) {
        Response createResponse = new Response(RespDefine.ERR_CODE_TASK_CREATE_FAILED, RespDefine.ERR_DESC_TASK_CREATE_FAILED);
        User user = userRepository.findByUserId(publisherID);

        //用户信息错误
        if (null == user || user.getCurrencyNumber() < 0) {
            LOGGER.error("publisher id is invalid.");
            return createResponse;
        }

        //余额不足，无法悬赏
        if (user.getCurrencyNumber() < bounty) {
            LOGGER.error("current currency number not enough.");
            return createResponse;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Task task = new Task(publisherID, taskName, timestamp, TaskStatus.ONGOING);

        taskRepository.saveAndFlush(task);

        createResponse = new Response(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);

        return createResponse;
    }

    /**
     * 关闭任务
     * @param taskId 任务ID
     * @param employeeID 工号
     * @return 结果
     */
    @Override
    public Response finish(int taskId, String employeeID) {

        Response finishTaskResponse = new Response(RespDefine.ERR_CODE_TASK_FINISH_FAILED,
                RespDefine.ERR_DESC_TASK_FINISH_FAILED);

        Task task = taskRepository.findByTaskID(taskId);

        if (null == task || null == task.getTaskStatus() || task.getTaskStatus().equals(TaskStatus.CLOSED)) {
            LOGGER.error("task status error.");
            return finishTaskResponse;
        }

        //通过session对操作用户进行校验
        User user = userRepository.findByEmployeeID(employeeID);

        if (null == user || user.getUserId() <= 0) {
            LOGGER.error("current user session invalid.");
            return finishTaskResponse;
        }

        if (task.getPublisherID() != user.getUserId()) {
            LOGGER.error("current user is not publisher.");
            return finishTaskResponse;
        }

        //设置任务为关闭状态
        task.setTaskStatus(TaskStatus.CLOSED);

        taskRepository.saveAndFlush(task);

        finishTaskResponse = new Response(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);

        return finishTaskResponse;
    }

    @Override
    public TaskResponse queryAll() {
        TaskResponse queryAllTaskResponse = new TaskResponse(RespDefine.ERR_CODE_TASK_QUERY_FAILED,
                RespDefine.ERR_DESC_TASK_QUERY_FAILED);

        List<Task> tasks = taskRepository.findAll(Sort.by(Sort.Order.desc(PUBLISH_TIME_NAME)));

        if (null != tasks) {
            queryAllTaskResponse = new TaskResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, tasks);
        } else {
            LOGGER.error("query all tasks failed.");
        }

        return queryAllTaskResponse;
    }
}
