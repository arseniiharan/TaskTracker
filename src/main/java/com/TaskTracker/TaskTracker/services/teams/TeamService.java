package com.TaskTracker.TaskTracker.services.teams;

import com.TaskTracker.TaskTracker.DTO.teams.TeamCreateDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamUserDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDeleteDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.teams.*;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    void createTeam(TeamCreateDTO teamCreateDTO) throws TeamAlreadyExistsException, TeamLeaderRestriction, UserNeverExistedException;
    TeamDTO getTeam(String teamName) throws TeamNeverExistedException;
    void addUserTeam(UUID currentUserId, TeamUserDTO teamUserDTO) throws TeamNeverExistedException, UserNeverExistedException, UserAlreadyInTeamException, TeamLeaderRestriction;
    void removeUserTeam(UUID currentUserId, TeamUserDTO teamUserDTO) throws TeamNeverExistedException, UserNeverExistedException, UserNotInTeamException, TeamLeaderRestriction;
    List<UserResponseDTO> getTeamMembers(UUID teamId) throws TeamNeverExistedException;
    void deleteTeam(TeamDeleteDTO teamDeleteDTO) throws TeamNeverExistedException, UserNeverExistedException, TeamLeaderRestriction;
}
