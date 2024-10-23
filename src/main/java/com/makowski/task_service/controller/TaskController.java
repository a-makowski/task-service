package com.makowski.task_service.controller;

import com.makowski.task_service.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    //endpoint for testing purposes
    @GetMapping(value = "/username")
    public ResponseEntity<String> currentUserName(HttpServletRequest request) {
        return new ResponseEntity<>((String) request.getAttribute("username"), HttpStatus.OK);
    }


    //add task

    //delete task

    //rename task

    //change status

    //get task


}
