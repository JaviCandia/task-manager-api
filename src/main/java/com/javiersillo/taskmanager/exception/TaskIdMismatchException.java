package com.javiersillo.taskmanager.exception;

public class TaskIdMismatchException extends RuntimeException {
    public TaskIdMismatchException(String message) {
        super(message);
    }
}
