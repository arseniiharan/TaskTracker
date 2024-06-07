package com.TaskTracker.TaskTracker.controllers;

import com.TaskTracker.TaskTracker.DTO.tasks.*;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskAlreadyExists;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.tasks.TaskNotInTheTeamException;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamLeaderRestriction;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.services.tasks.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("{teamId}/create")
    public ResponseEntity<?> createNewTask(@PathVariable UUID teamId, @RequestBody CreateTeamTaskDTO createTeamTaskDTO) {
        try {
            TaskDTO taskDTO = taskService.createTeamTask(createTeamTaskDTO);
            return ResponseEntity.ok(taskDTO);
        } catch (TaskAlreadyExists e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @GetMapping("{teamId}/get/{taskId}")
    public ResponseEntity<?> getTeamTask(@PathVariable UUID teamId, @PathVariable UUID taskId) {
        try {
            TaskDTO taskDTO = taskService.getTeamTaskById(teamId, taskId);
            return ResponseEntity.ok(taskDTO);
        } catch (TaskNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @DeleteMapping("{teamId}/delete")
    public ResponseEntity<?> deleteTask(@PathVariable UUID teamId, @RequestBody DeleteTeamTaskDTO deleteTeamTaskDTO) {
        try {
            taskService.removeTeamTask(deleteTeamTaskDTO);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (TaskNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @PutMapping("{teamId}/update")
    public ResponseEntity<?> updateTeamTask(@PathVariable UUID teamId, @RequestBody ChangeTeamTaskDTO changeTaskDTO) {
        try {
            TaskDTO taskDTO = taskService.updateTeamTask(changeTaskDTO);
            return ResponseEntity.ok(taskDTO);
        } catch (TaskNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("{teamId}/add")
    public ResponseEntity<?> addUserTask(@PathVariable UUID teamId, @RequestParam UUID currentUserId, @RequestBody UserTaskDTO userTaskDTO) {
        try {
            taskService.addUserTask(currentUserId, userTaskDTO);
            return ResponseEntity.ok("Task added successfully");
        } catch (TaskNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotInTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotInTheTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @DeleteMapping("{teamId}/remove")
    public ResponseEntity<?> removeTask(@PathVariable UUID teamId, @RequestParam UUID currentUserId, @RequestBody UserTaskDTO userTaskDTO) {
        try {
            taskService.removeUserTask(currentUserId, userTaskDTO);
            return ResponseEntity.ok("Task added successfully");
        } catch (TaskNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotInTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotInTheTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @GetMapping("{teamId}/get-tasks")
    public ResponseEntity<?> getAllTeamTasks(@PathVariable UUID teamId) {
        try {
            List<TaskDTO> taskDTOs = taskService.getAllTeamTasks(teamId);
            return ResponseEntity.ok(taskDTOs);
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }
}
