package com.makowski.task_service.service;

import com.makowski.task_service.entity.Task;
import com.makowski.task_service.exceptions.AccessDeniedException;
import com.makowski.task_service.exceptions.InvalidRequestException;
import com.makowski.task_service.exceptions.EntityNotFoundException;
import com.makowski.task_service.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

    public Task getTaskByID(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Task.class, id));
    }

    public void ownerCheck(String loggedUser, String taskOwner) {
        if (!loggedUser.equals(taskOwner)) throw new AccessDeniedException();
    }

    public Task addTask(String username, String title) {
        Task task = new Task();
        task.setOwner(username);
        task.setTitle(title);
        task.setStatus(false);
        return taskRepository.save(task);
    }

    public void deleteTask(String username, Long id) {
        Task task = getTaskByID(id);
        ownerCheck(username, task.getOwner());
        taskRepository.deleteById(id);
    }

    public Task changeTitle(String username, Long id, String title) {
        Task task = getTaskByID(id);
        ownerCheck(username, task.getOwner());
        if (title.isBlank() || title.isEmpty()) throw new InvalidRequestException("Incorrect task title");
        task.setTitle(title);
        return taskRepository.save(task);
    }

    public Task changeStatus(String username, Long id) {
        Task task = getTaskByID(id);
        ownerCheck(username, task.getOwner());
        task.setStatus(!task.isStatus());
        return taskRepository.save(task);
    }

    public Task getTask(String username, Long id) {
        Task task = getTaskByID(id);
        ownerCheck(username, task.getOwner());
        return task;
    }

    public List<Task> getMyTasks(String username) {
        List<Task> tasks = taskRepository.findByOwner(username);
        if (!tasks.isEmpty()) return tasks;
            else throw new EntityNotFoundException("There are no tasks available for you");
    }
}