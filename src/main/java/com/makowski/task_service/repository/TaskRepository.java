package com.makowski.task_service.repository;

import com.makowski.task_service.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    public List<Task> findByOwner(String owner);
}