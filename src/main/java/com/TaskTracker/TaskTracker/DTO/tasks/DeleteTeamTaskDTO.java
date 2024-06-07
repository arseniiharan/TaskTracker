package com.TaskTracker.TaskTracker.DTO.tasks;

import java.util.UUID;

public record DeleteTeamTaskDTO(UUID userId, UUID teamId, String taskName) {
}
