package com.TaskTracker.TaskTracker.services.users;

import com.TaskTracker.TaskTracker.DTO.users.UserRegistrationRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;

import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserAlreadyExistsException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.models.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRegistrationRequestDTO userRegistrationRequestDTO) throws UserAlreadyExistsException;
    void activateUser(String activationCode) throws UserNeverExistedException;
    UserResponseDTO userFindByEmail(String email) throws UserNeverExistedException;
    List<UserResponseDTO> usersFindByTeam(String teamName) throws TeamNeverExistedException, UserNotInTeamException;
    void deleteUser(String email) throws UserNeverExistedException;
}
