package com.TaskTracker.TaskTracker.services.teams.impl;

import com.TaskTracker.TaskTracker.DTO.teams.TeamCreateDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamUserDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDeleteDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.teams.*;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.mappers.TeamMapper;
import com.TaskTracker.TaskTracker.mappers.UserMapper;
import com.TaskTracker.TaskTracker.models.Team;
import com.TaskTracker.TaskTracker.models.User;
import com.TaskTracker.TaskTracker.repositories.TeamRepository;
import com.TaskTracker.TaskTracker.repositories.UserRepository;
import com.TaskTracker.TaskTracker.services.teams.TeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final UserMapper userMapper;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createTeam(TeamCreateDTO teamCreateDTO) throws TeamAlreadyExistsException, TeamLeaderRestriction, UserNeverExistedException {
        // User existence check
        Optional<User> userOptional = userRepository.findById(teamCreateDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // Team existence check
        if (teamRepository.findByTeamName(teamCreateDTO.teamName()).isPresent()) {
            throw new TeamAlreadyExistsException("Team with name like this already exists");
        }

        // User check for team leader role
        if (!user.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to create teams");
        }

        Team team = new Team();
        team.setTeamName(teamCreateDTO.teamName());
        user.getTeams().add(team);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public TeamDTO getTeam(String teamName) throws TeamNeverExistedException {
        Optional<Team> teamOptional = teamRepository.findByTeamName(teamName);
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        return teamMapper.toDto(team);
    }

    @Override
    @Transactional
    public void addUserTeam(UUID currentUserId, TeamUserDTO teamUserDTO) throws UserNeverExistedException, TeamNeverExistedException, UserAlreadyInTeamException, TeamLeaderRestriction {
        // User who's being added existence check
        Optional<User> userOptional = userRepository.findById(teamUserDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // Team existence check
        Optional<Team> teamOptional = teamRepository.findById(teamUserDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        // User who's adding teammates existence check
        Optional<User> currentUserOptional = userRepository.findById(currentUserId);
        User currentUser = currentUserOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // User who's adding teammates check for team leader role
        if (!currentUser.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to add team mates");
        }

        // Check if user is already in the team
        if (user.getTeams().contains(team)) {
            throw new UserAlreadyInTeamException("This user already in team");
        }

        // Getting all user teams and adding a new one, saving user after this
        user.getTeams().add(team);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUserTeam(UUID currentUserId, TeamUserDTO teamUserDTO) throws TeamNeverExistedException, UserNeverExistedException, UserNotInTeamException, TeamLeaderRestriction {
        // User who's being added existence check
        Optional<User> userOptional = userRepository.findById(teamUserDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // Team existence check
        Optional<Team> teamOptional = teamRepository.findById(teamUserDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        // User who's adding teammates existence check
        Optional<User> currentUserOptional = userRepository.findById(currentUserId);
        User currentUser = currentUserOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // User who's adding teammates check for team leader role
        if (!currentUser.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to add team mates");
        }

        // Check if user is in the team
        if (!user.getTeams().contains(team)) {
            throw new UserNotInTeamException("User doesn't belong to this team or was never added");
        }

        user.getTeams().remove(team);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<UserResponseDTO> getTeamMembers(UUID teamId) throws TeamNeverExistedException {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        List<User> usersWithTeam = userRepository.getAllUsersWithTeam(teamId);
        List<UserResponseDTO> usersWithTeamDTOs = new ArrayList<>();

        for (User u : usersWithTeam) {
            usersWithTeamDTOs.add(userMapper.toDto(u));
        }
        return usersWithTeamDTOs;
    }

    @Override
    @Transactional
    public void deleteTeam(TeamDeleteDTO teamCreateDeleteDTO) throws TeamNeverExistedException, UserNeverExistedException, TeamLeaderRestriction {
        // User existence check
        Optional<User> userOptional = userRepository.findById(teamCreateDeleteDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // Team existence check
        Optional<Team> teamOptional = teamRepository.findById(teamCreateDeleteDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));


        if (!user.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to delete teams");
        }

        // Getting list with all users with team like this and deleting team from all users for app to work proper
        List<User> usersWithTeam = userRepository.getAllUsersWithTeam(team.getId());

        for (User u : usersWithTeam) {
            u.getTeams().remove(team);
        }

        userRepository.saveAll(usersWithTeam);
        teamRepository.delete(team);
    }
}
