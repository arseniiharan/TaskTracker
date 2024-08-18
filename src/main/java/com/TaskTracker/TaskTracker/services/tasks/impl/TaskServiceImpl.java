package com.TaskTracker.TaskTracker.services.tasks.impl;

import com.TaskTracker.TaskTracker.DTO.tasks.*;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskAlreadyExists;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNotInTheTeamException;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamLeaderRestriction;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.mappers.TaskMapper;
import com.TaskTracker.TaskTracker.models.Task;
import com.TaskTracker.TaskTracker.models.Team;
import com.TaskTracker.TaskTracker.models.User;
import com.TaskTracker.TaskTracker.repositories.TaskRepository;
import com.TaskTracker.TaskTracker.repositories.TeamRepository;
import com.TaskTracker.TaskTracker.repositories.UserRepository;
import com.TaskTracker.TaskTracker.services.tasks.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskDTO createTeamTask(CreateTeamTaskDTO createTeamTaskDTO) throws TaskAlreadyExists, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction {
        // Checking if task with the same name already exists
        if (taskRepository.findByTaskName(createTeamTaskDTO.taskName()).isPresent()) {
            throw new TaskAlreadyExists("Task with this name already exists");
        }

        // Checking team existence
        Optional<Team> teamOptional = teamRepository.findById(createTeamTaskDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        // Checking user existence
        Optional<User> userOptional = userRepository.findById(createTeamTaskDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        // Checking user rights to create tasks for team
        if (!user.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to create team tasks");
        }

        // Creating an empty task and filling it up after
        Task task = new Task();
        task.setTaskName(createTeamTaskDTO.taskName());
        task.setStartDate(createTeamTaskDTO.startDate());
        task.setEndDate(createTeamTaskDTO.endDate());
        task.setTaskImportance(createTeamTaskDTO.taskImportance());

        // Setting the task to the team and saving the team after
        team.getTasks().add(task);
        teamRepository.save(team);

        // Saving the task
        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskDTO getTeamTaskById(UUID teamId, UUID taskId) throws TaskNeverExistedException, TeamNeverExistedException {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Task task = taskOptional.orElseThrow(() -> new TaskNeverExistedException("This task doesn't exist"));

        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public void removeTeamTask(DeleteTeamTaskDTO deleteTeamTaskDTO) throws UserNeverExistedException, TeamNeverExistedException, TaskNeverExistedException, TeamLeaderRestriction {
        Optional<Task> taskOptional = taskRepository.findByTaskName(deleteTeamTaskDTO.taskName());
        Task task = taskOptional.orElseThrow(() -> new TaskNeverExistedException("Task with this name doesn't exist"));

        Optional<Team> teamOptional = teamRepository.findById(deleteTeamTaskDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("Team like this doesn't exist"));

        Optional<User> userOptional = userRepository.findById(deleteTeamTaskDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("User like this doesn't exist"));

        if (!user.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to delete team tasks");
        }

        // Creating specified list with users who are in the team
        List<User> usersWithTeam = userRepository.getAllUsersWithTeam(team.getId());

        // Scrolling through all users
        for (User u : usersWithTeam) {
            // Searching if any teammate have the specified task
            if (u.getTasks().contains(task)) {
                // Deleting the specified task from user if they have it
                u.getTasks().remove(task);
                // Saving user without task
                userRepository.save(u);
            }
        }

        // Deleting task from team
        team.getTasks().remove(task);
        teamRepository.save(team);

        // Deleting task itself
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskDTO updateTeamTask(ChangeTeamTaskDTO changeTeamTaskDTO) throws TaskNeverExistedException, TeamNeverExistedException, UserNeverExistedException, TeamLeaderRestriction {
        Optional<Task> taskOptional = taskRepository.findByTaskName(changeTeamTaskDTO.taskName());
        Task task = taskOptional.orElseThrow(() -> new TaskNeverExistedException("Task with this name doesn't exist"));

        Optional<Team> teamOptional = teamRepository.findById(changeTeamTaskDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        Optional<User> userOptional = userRepository.findById(changeTeamTaskDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("This user doesn't exist"));

        if (!user.isTeamLeader()) {
            throw new TeamLeaderRestriction("User has to be Team Leader to change team tasks");
        }

        // Updating the task with new parameters
        task.setId(changeTeamTaskDTO.taskId());
        task.setTaskName(changeTeamTaskDTO.taskName());
        task.setStartDate(changeTeamTaskDTO.startDate());
        task.setEndDate(changeTeamTaskDTO.endDate());
        task.setTaskImportance(changeTeamTaskDTO.taskImportance());

        // Saving the edited task
        taskRepository.save(task);

        List<User> usersWithTeam = userRepository.getAllUsersWithTeam(changeTeamTaskDTO.teamId());

        // Deleting the old task from users who have it and setting the updated one to avoid conflicts
        for (User u : usersWithTeam) {
            if (u.getTasks().contains(task)) {
                u.getTasks().remove(task);
                u.getTasks().add(task);
                userRepository.save(u);
            }
        }

        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public void addUserTask(UUID currentUserId, UserTaskDTO userTaskDTO) throws TaskNeverExistedException, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction, UserNotInTeamException, TaskNotInTheTeamException {
        Optional<Task> taskOptional = taskRepository.findById(userTaskDTO.taskId());
        Task task = taskOptional.orElseThrow(() -> new TaskNeverExistedException("This task doesn't exist"));

        Optional<User> userOptional = userRepository.findById(userTaskDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("User doesn't exist"));

        Optional<Team> teamOptional = teamRepository.findById(userTaskDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        Optional<User> currentUserOptional = userRepository.findById(currentUserId);
        User currentUser = currentUserOptional.orElseThrow(() -> new UserNeverExistedException("User who's trying to add doesn't exist"));

        if (!currentUser.isTeamLeader()) {
            throw new TeamLeaderRestriction("Only Team Leader can add tasks for teammates");
        }

        if (!team.getTasks().contains(task)) {
            throw new TaskNotInTheTeamException("Task wasn't created or not assigned to this team");
        }

        if (!user.getTeams().contains(team)) {
            throw new UserNotInTeamException("User who's getting task added not in the current team");
        }

        user.getTasks().add(task);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUserTask(UUID currentUserId, UserTaskDTO userTaskDTO) throws TaskNeverExistedException, UserNeverExistedException, TeamNeverExistedException, TeamLeaderRestriction, UserNotInTeamException, TaskNotInTheTeamException {
        Optional<Task> taskOptional = taskRepository.findById(userTaskDTO.taskId());
        Task task = taskOptional.orElseThrow(() -> new TaskNeverExistedException("This task doesn't exist"));

        Optional<User> userOptional = userRepository.findById(userTaskDTO.userId());
        User user = userOptional.orElseThrow(() -> new UserNeverExistedException("User doesn't exist"));

        Optional<Team> teamOptional = teamRepository.findById(userTaskDTO.teamId());
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        Optional<User> currentUserOptional = userRepository.findById(currentUserId);
        User currentUser = currentUserOptional.orElseThrow(() -> new UserNeverExistedException("User who's trying to add doesn't exist"));

        if (!currentUser.isTeamLeader()) {
            throw new TeamLeaderRestriction("Only Team Leader can add tasks for teammates");
        }

        if (!team.getTasks().contains(task)) {
            throw new TaskNotInTheTeamException("Task wasn't created or not assigned to this team");
        }

        if (!user.getTeams().contains(team)) {
            throw new UserNotInTeamException("User who's getting task deleted not in the current team");
        }

        user.getTasks().remove(task);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<TaskDTO> getAllTeamTasks(UUID teamId) throws TeamNeverExistedException {
        // Checking for the team existence
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Team team = teamOptional.orElseThrow(() -> new TeamNeverExistedException("This team doesn't exist"));

        // Creating list with all tasks from team
        List<Task> tasksInTeam = taskRepository.getAllTasksWithinTeam(teamId);

        // Creating specified list for all tasks from team but in DTO form
        List<TaskDTO> tasksInTeamDTO = new ArrayList<>();

        // Getting all the tasks one by one, converting into DTO and putting into specified list
        for (Task t : tasksInTeam) {
            tasksInTeamDTO.add(taskMapper.toDto(t));
        }

        // Returning the specified list with DTOs
        return tasksInTeamDTO;
    }
}
