package com.TaskTracker.TaskTracker.DTO.tasks;

import java.util.UUID;

public record ChangeTeamTaskDTO(UUID userId, UUID teamId, UUID taskId, String taskName, String startDate, String endDate, String taskImportance) {
}
