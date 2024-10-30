package com.makowski.task_service.exceptions;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String problem) {
        super(problem);
    }
}