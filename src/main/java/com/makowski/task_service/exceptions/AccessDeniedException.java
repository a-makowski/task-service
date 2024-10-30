package com.makowski.task_service.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("You have no access to this data");
    }
}