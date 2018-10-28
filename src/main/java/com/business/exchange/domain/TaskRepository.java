package com.business.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTaskId(int taskId);

    List<Task> findByPublisherIDOrderByPublishTimeDesc(int userId);
}