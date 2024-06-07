package com.TaskTracker.TaskTracker.DTO.tasks;

import java.util.UUID;

public record UserTaskDTO(UUID userId, UUID teamId, UUID taskId){
}
