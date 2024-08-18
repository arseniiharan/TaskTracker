package com.TaskTracker.TaskTracker.controllers;

import com.TaskTracker.TaskTracker.DTO.teams.TeamCreateDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamDeleteDTO;
import com.TaskTracker.TaskTracker.DTO.teams.TeamUserDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.teams.*;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.services.teams.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateDTO teamCreateDTO) {
        try {
            teamService.createTeam(teamCreateDTO);
            return ResponseEntity.ok("Team created!");
        } catch (TeamAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @GetMapping("find/{teamName}")
    public ResponseEntity<?> findTeam(@PathVariable String teamName) {
        try {
            TeamDTO teamDTO = teamService.getTeam(teamName);
            return ResponseEntity.ok(teamDTO);
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @PostMapping("/{teamId}/adduser")
    public ResponseEntity<?> addUserTeam(@PathVariable UUID teamId, @RequestParam UUID currentUserId, @RequestBody TeamUserDTO teamUserDTO) {
        try {
            teamService.addUserTeam(currentUserId, teamUserDTO);
            return ResponseEntity.ok("User added to team");
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserAlreadyInTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @DeleteMapping("/{teamId}/remove-user")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable UUID teamId, @RequestParam UUID currentUserId, @RequestBody TeamUserDTO teamUserDTO) {
        try {
            teamService.removeUserTeam(currentUserId, teamUserDTO);
            return ResponseEntity.ok("User removed from team");
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotInTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @GetMapping("/find-members/{teamId}")
    public ResponseEntity<?> getAllTeamMembers(@PathVariable UUID teamId) {
        try {
            List<UserResponseDTO> userResponseDTOList = teamService.getTeamMembers(teamId);
            return ResponseEntity.ok(userResponseDTOList);
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTeam(@RequestBody TeamDeleteDTO teamDeleteDTO) {
        try {
            teamService.deleteTeam(teamDeleteDTO);
            return ResponseEntity.ok("Team deleted successfully");
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeamLeaderRestriction e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }
}
