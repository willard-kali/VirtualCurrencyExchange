package com.business.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTaskID(int taskId);
}
