package com.business.exchange.domain;

import com.business.exchange.model.TaskStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskID;

    private int publisherID;

    private String taskName;

    private int bounty;

    private Timestamp publishTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    public Task() {
    }

    public Task(int publisherID, String taskName, Timestamp publishTime, TaskStatus taskStatus) {
        this.publisherID = publisherID;
        this.taskName = taskName;
        this.publishTime = publishTime;
        this.taskStatus = taskStatus;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getPublisherID() {
        return publisherID;
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
