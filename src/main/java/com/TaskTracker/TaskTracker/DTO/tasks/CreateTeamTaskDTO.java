package com.TaskTracker.TaskTracker.DTO.tasks;

import java.util.UUID;

public record CreateTeamTaskDTO(UUID userId, UUID teamId, String taskName, String startDate, String endDate, String taskImportance) {
}
