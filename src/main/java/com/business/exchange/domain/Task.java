package com.business.exchange.domain;

import com.business.exchange.model.TaskStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskId;

    private int publisherID;

    private String publisherEmployeeID;

    private String publisherName;

    private String taskName;

    private int bounty;

    private Timestamp publishTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    public Task() {
    }

    public Task(int publisherID, String publisherEmployeeID, String publisherName, String taskName, int bounty, Timestamp publishTime, TaskStatus taskStatus) {
        this.publisherID = publisherID;
        this.publisherEmployeeID = publisherEmployeeID;
        this.publisherName = publisherName;
        this.taskName = taskName;
        this.bounty = bounty;
        this.publishTime = publishTime;
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public String getPublisherEmployeeID() {
        return publisherEmployeeID;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getBounty() {
        return bounty;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }
}
