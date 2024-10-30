package com.makowski.task_service.controller;

import com.makowski.task_service.entity.Task;
import com.makowski.task_service.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> addTask(HttpServletRequest request, @RequestBody String title) {
        return new ResponseEntity<>(taskService.addTask((String) request.getAttribute("username"), title), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(HttpServletRequest request, @PathVariable Long id) {
        taskService.deleteTask((String) request.getAttribute("username"), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/title/{id}")
    public ResponseEntity<Task> changeTitle(HttpServletRequest request, @PathVariable Long id, @RequestBody String title) {
        return new ResponseEntity<>(taskService.changeTitle((String) request.getAttribute("username"), id, title), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Task> changeStatus(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(taskService.changeStatus((String) request.getAttribute("username"), id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTask((String) request.getAttribute("username"), id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getMyTasks(HttpServletRequest request) {
        return new ResponseEntity<>(taskService.getMyTasks((String) request.getAttribute("username")), HttpStatus.OK);
    }

}