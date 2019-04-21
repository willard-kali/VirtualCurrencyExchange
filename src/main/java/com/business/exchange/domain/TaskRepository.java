package com.business.exchange.domain;

import com.business.exchange.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTaskId(int taskId);

    List<Task> findByPublisherIDOrderByPublishTimeDesc(int userId);

    List<Task> findByPublisherIDAndTaskNameIsLikeOrderByPublishTimeDesc(int userId, String taskNameLike);

    List<Task> findAllByTaskNameLikeOrderByPublishTimeDesc(String taskNameLike);

    List<Task> findAllByTaskStatusEqualsOrderByPublishTimeDesc(TaskStatus taskStatus);
}
