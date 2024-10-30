package com.makowski.task_service.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entity, Long id) {
        super("The " + entity.getSimpleName().toLowerCase() + " with ID " + id + " doesn't exist");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}