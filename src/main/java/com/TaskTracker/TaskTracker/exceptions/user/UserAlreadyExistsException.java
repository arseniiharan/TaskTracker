package com.TaskTracker.TaskTracker.exceptions.user;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
