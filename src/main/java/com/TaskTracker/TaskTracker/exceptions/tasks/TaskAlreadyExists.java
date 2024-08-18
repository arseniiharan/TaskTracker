package com.TaskTracker.TaskTracker.exceptions.tasks;

public class TaskAlreadyExists extends Exception{
    public TaskAlreadyExists(String message) {
        super(message);
    }
}
