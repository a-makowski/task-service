package com.makowski.task_service.service;

import com.makowski.task_service.entity.Task;
import com.makowski.task_service.exceptions.AccessDeniedException;
import com.makowski.task_service.exceptions.EntityNotFoundException;
import com.makowski.task_service.exceptions.InvalidRequestException;
import com.makowski.task_service.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;

    @Test
    void getTaskByID_ReturnsTask_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskByID(1L);

        assertEquals(task, result);
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskByID_ThrowsException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskByID(1L));
    }

    @Test
    void ownerCheck_ThrowsException_WhenUsersDoNotMatch() {
        assertThrows(AccessDeniedException.class, () -> taskService.ownerCheck("user1", "user2"));
    }

    @Test
    void titleCheck_ThrowsException_WhenEmptyTitle() {
        assertThrows(InvalidRequestException.class, () -> taskService.titleCheck(""));
    }

    @Test
    void titleCheck_ThrowsException_WhenBlankTitle() {
        assertThrows(InvalidRequestException.class, () -> taskService.titleCheck(" "));
    }

    @Test
    void addTask_AddsTask_WhenCorrectTitle() {
        Task task = new Task();
        task.setOwner("user");
        task.setTitle("title");
        task.setStatus(false);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.addTask("user", "title");

        assertEquals(task, result);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void addTask_ThrowsException_WhenInvalidTitle() {
        assertThrows(InvalidRequestException.class, () -> taskService.addTask("username", "  "));
    }

    @Test
    void deleteTask_DeletesTask_WhenTaskExistsAndUsernamesMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask("user", 1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_ThrowsException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask("user", 1L));
    }

    @Test
    void deleteTask_ThrowsException_WhenUsernamesDoesNotMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.deleteTask("anotherUser", 1L));
    }

    @Test
    void changeTitle_ChangesTitle_WhenUsernameAndTitleAreCorrect() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.changeTitle("user", 1L, "new title");

        assertEquals("new title", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void changeTitle_ThrowsException_WhenUsernamesDoesNotMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class,
                () -> taskService.changeTitle("wrongUser", 1L, "New Title"));
    }

    @Test
    void changeTitle_ThrowsException_WhenInvalidTitle() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("owner");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(InvalidRequestException.class,
                () -> taskService.changeTitle("owner", 1L, " "));
    }

    @Test
    void changeStatus_ChangesStatus_WhenTaskExistAndUsernamesMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        task.setStatus(false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.changeStatus("user", 1L);

        assertTrue(result.isStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void changeStatus_ThrowsException_WhenUsernamesDoNotMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.changeStatus("anotherUser", 1L));
    }

    @Test
    void changeStatus_ThrowsException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.changeStatus("user", 1L));
    }

    @Test
    void getTask_ReturnsTask_WhenTaskExistAndUsernamesMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTask("user", 1L);

        assertEquals(task, result);
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTask_ThrowsException_WhenUsernamesDoNotMatch() {
        Task task = new Task();
        task.setId(1L);
        task.setOwner("user");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.getTask("anotherUser", 1L));
    }

    @Test
    void getTask_ThrowsException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTask("user", 1L));
    }

    @Test
    void getMyTasks() {
        List<Task> tasks = List.of(new Task());
        when(taskRepository.findByOwner("user")).thenReturn(tasks);

        List<Task> result = taskService.getMyTasks("user");

        assertEquals(tasks, result);
        verify(taskRepository).findByOwner("user");
    }

    @Test
    void getMyTasks_throwsEntityNotFoundException() {
        when(taskRepository.findByOwner("user")).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> taskService.getMyTasks("user"));
    }
}