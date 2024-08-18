package com.TaskTracker.TaskTracker.services.tasks;

import com.TaskTracker.TaskTracker.DTO.tasks.*;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskAlreadyExists;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNotInTheTeamException;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamLeaderRestriction;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDTO createTeamTask(CreateTeamTaskDTO createTeamTaskDTO) throws TaskAlreadyExists, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction;
    TaskDTO getTeamTaskById(UUID teamId, UUID taskId) throws TaskNeverExistedException, TeamNeverExistedException;
    void removeTeamTask(DeleteTeamTaskDTO deleteTeamTaskDTO) throws UserNeverExistedException, TeamNeverExistedException, TaskNeverExistedException, TeamLeaderRestriction;
    TaskDTO updateTeamTask(ChangeTeamTaskDTO changeTeamTaskDTO) throws TaskNeverExistedException, TeamNeverExistedException, UserNeverExistedException, TeamLeaderRestriction;
    void addUserTask(UUID currentUserId, UserTaskDTO userTaskDTO) throws TaskNeverExistedException, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction, UserNotInTeamException, TaskNotInTheTeamException;
    void removeUserTask(UUID currentUserId, UserTaskDTO userTaskDTO) throws TaskNeverExistedException, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction, UserNotInTeamException, TaskNotInTheTeamException;
    List<TaskDTO> getAllTeamTasks(UUID teamId) throws TeamNeverExistedException;
}
