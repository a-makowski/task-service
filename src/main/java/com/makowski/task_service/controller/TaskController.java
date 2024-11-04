package com.makowski.task_service.controller;

import com.makowski.task_service.entity.Task;
import com.makowski.task_service.exceptions.ErrorResponse;
import com.makowski.task_service.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/task")
@Tag(name = "Task Controller", description = "Task entity manager")
public class TaskController {

    private TaskService taskService;

    @Operation(summary = "Add new task", description = "Creates a new task with the provided title for the currently logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been successfully created", content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect title for the task", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<Task> addTask(HttpServletRequest request, @RequestBody(required = false) String title) {
        return new ResponseEntity<>(taskService.addTask((String) request.getAttribute("username"), title), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete task", description = "Deletes the task with the provided ID. Task can only be deleted by its owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task has been successfully deleted"),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "There is no task with the given ID in the database", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(HttpServletRequest request, @PathVariable Long id) {
        taskService.deleteTask((String) request.getAttribute("username"), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Change task title", description = "Changes the title of the task with the provided ID. Task can only be edited by its owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Title has been successfully changed", content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect title for the task", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "There is no task with the given ID in the database", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/title/{id}")
    public ResponseEntity<Task> changeTitle(HttpServletRequest request, @PathVariable Long id, @RequestBody(required = false) String title) {
        return new ResponseEntity<>(taskService.changeTitle((String) request.getAttribute("username"), id, title), HttpStatus.OK);
    }

    @Operation(summary = "Mark task as done/undone", description = "Changes the status of the task to the opposite. Task can only be edited by its owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status has been successfully changed", content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect title for the task", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "There is no task with the given ID in the database", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/status/{id}")
    public ResponseEntity<Task> changeStatus(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(taskService.changeStatus((String) request.getAttribute("username"), id), HttpStatus.OK);
    }

    @Operation(summary = "Get task", description = "Returns the task with the provided ID. Only the task owner can get it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task has been successfully returned", content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "There is no task with the given ID in the database", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTask((String) request.getAttribute("username"), id), HttpStatus.OK);
    }

    @Operation(summary = "Get all my tasks", description = "Returns a list of all tasks owned by the currently logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task list has been successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class)))),
            @ApiResponse(responseCode = "401", description = "JWT Token not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currently logged in user does not have any tasks", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Task>> getMyTasks(HttpServletRequest request) {
        return new ResponseEntity<>(taskService.getMyTasks((String) request.getAttribute("username")), HttpStatus.OK);
    }

}