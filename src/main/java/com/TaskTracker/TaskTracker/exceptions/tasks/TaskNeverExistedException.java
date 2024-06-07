package com.TaskTracker.TaskTracker.exceptions.tasks;

public class TaskNeverExistedException extends Exception{
    public TaskNeverExistedException(String message) {
        super(message);
    }
}
