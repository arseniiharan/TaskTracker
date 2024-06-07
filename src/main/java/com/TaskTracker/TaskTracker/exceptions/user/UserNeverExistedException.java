package com.TaskTracker.TaskTracker.exceptions.user;

public class UserNeverExistedException extends Exception{
    public UserNeverExistedException(String message) {
        super(message);
    }
}
