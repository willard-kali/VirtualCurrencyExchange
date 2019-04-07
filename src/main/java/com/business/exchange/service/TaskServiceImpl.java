package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Task;
import com.business.exchange.domain.TaskRepository;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserRepository;
import com.business.exchange.model.Pagination;
import com.business.exchange.model.TaskResponse;
import com.business.exchange.model.TaskStatus;
import com.business.exchange.model.TasksResponse;
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
     * @param employeeID 发布者工号
     * @param taskName 任务名称
     * @param bounty 悬赏金额
     * @return 发布状态
     */
    @Override
    public TaskResponse create(String employeeID, String taskName, int bounty) {
        TaskResponse createResponse = new TaskResponse(RespDefine.ERR_CODE_TASK_CREATE_FAILED, RespDefine.ERR_DESC_TASK_CREATE_FAILED);
        User user = userRepository.findByEmployeeID(employeeID);

        //用户信息错误
        if (null == user
                || user.getCurrencyNumber() < 0
                || user.getUserId() <= 0
                || user.getEmployeeID().isEmpty()
                || user.getUserName().isEmpty()) {
            LOGGER.error("publisher id is invalid.");
            return createResponse;
        }

        //余额不足，无法悬赏
        if (user.getCurrencyNumber() < bounty) {
            LOGGER.error("current currency number not enough.");
            return createResponse;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String taskReceiver = "";

        Task task = new Task(user.getUserId(), user.getEmployeeID(), user.getUserName(),
                taskName, bounty, timestamp, TaskStatus.ONGOING, taskReceiver);

        taskRepository.saveAndFlush(task);

        createResponse = new TaskResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, task);

        return createResponse;
    }

    /**
     * 关闭任务
     * @param taskId 任务ID
     * @param employeeID 工号
     * @return 结果
     */
    @Override
    public TaskResponse close(int taskId, String employeeID) {

        TaskResponse finishTaskResponse = new TaskResponse(RespDefine.ERR_CODE_TASK_FINISH_FAILED,
                RespDefine.ERR_DESC_TASK_FINISH_FAILED);

        Task task = taskRepository.findByTaskId(taskId);

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

        finishTaskResponse = new TaskResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, task);

        return finishTaskResponse;
    }

    @Override
    public TasksResponse queryAll(int currentPage, int pageSize, String expression) {
        TasksResponse queryAllTaskResponse = new TasksResponse(RespDefine.ERR_CODE_TASK_QUERY_FAILED,
                RespDefine.ERR_DESC_TASK_QUERY_FAILED);

        List<Task> tasks;
        if (!expression.isEmpty()) {
            expression = "%" + expression + "%";
            tasks = taskRepository.findAllByTaskNameLikeOrderByPublishTimeDesc(expression);
        } else {
            tasks = taskRepository.findAll(Sort.by(Sort.Order.desc(PUBLISH_TIME_NAME)));
        }

        Pagination pagination = new Pagination(tasks.size(), pageSize, currentPage);

        int pageBegin = (currentPage - 1) * pageSize;
        int pageEnd = currentPage * pageSize;

        if (pageEnd > tasks.size()) {
            pageEnd = tasks.size();
        }
        List<Task> pageTasks = tasks.subList(pageBegin, pageEnd);

        if (null != pageTasks) {
            queryAllTaskResponse = new TasksResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, pageTasks, pagination);
        } else {
            LOGGER.error("query all tasks failed.");
        }

        return queryAllTaskResponse;
    }

    @Override
    public TasksResponse queryMine(int currentPage, int pageSize, String employeeID, String expression) {
        TasksResponse myTaskResp = new TasksResponse(RespDefine.ERR_CODE_TASK_QUERY_FAILED, RespDefine.ERR_DESC_TASK_QUERY_FAILED);
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user || user.getUserId() <= 0) {
            LOGGER.error("current user session is invalid.");
            return myTaskResp;
        }

        List<Task> tasks;
        if (!expression.isEmpty()) {
            expression = "%" + expression + "%";
            tasks = taskRepository.findByPublisherIDAndTaskNameIsLikeOrderByPublishTimeDesc(user.getUserId(), expression);
        } else {
            tasks = taskRepository.findByPublisherIDOrderByPublishTimeDesc(user.getUserId());
        }

        int pageBegin = (currentPage - 1) * pageSize;
        int pageEnd = currentPage * pageSize;

        if (pageEnd > tasks.size()) {
            pageEnd = tasks.size();
        }
        List<Task> pageTasks = tasks.subList(pageBegin, pageEnd);

        if (null == pageTasks) {
            LOGGER.error("task query result null.");
            return myTaskResp;
        }

        Pagination pagination = new Pagination(tasks.size(), pageSize, currentPage);

        myTaskResp = new TasksResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, pageTasks, pagination);

        return myTaskResp;
    }

    @Override
    public boolean receive(String employeeID, int taskId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (null == task) {
            LOGGER.error("task not exists.");
            return false;
        }
        if (TaskStatus.CLOSED.equals(task.getTaskStatus())) {
            LOGGER.error("task was already closed.");
            return false;
        }
        String taskReceiver = task.getTaskReceiver();
        if (taskReceiver.contains(employeeID)) {
            LOGGER.error("already exists.");
            return false;
        }
        if (taskReceiver.isEmpty()) {
            task.setTaskReceiver(employeeID);
        } else {
            task.setTaskReceiver(taskReceiver + ", " + employeeID);
        }
        taskRepository.saveAndFlush(task);
        return true;
    }

}
