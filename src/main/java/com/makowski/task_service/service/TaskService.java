package com.makowski.task_service.service;

import com.makowski.task_service.entity.Task;
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
                .orElseThrow(RuntimeException::new);     //TODO change exception
    }

    public void ownerCheck(String loggedUser, String taskOwner) {
        if (!loggedUser.equals(taskOwner)) throw new RuntimeException();  //TODO change exception
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
        ownerCheck(username, task.getOwner());                      // TODO change exception
        if (title.isBlank() || title.isEmpty()) throw new RuntimeException();
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
            else throw new RuntimeException();  //TODO change exc.
    }
}