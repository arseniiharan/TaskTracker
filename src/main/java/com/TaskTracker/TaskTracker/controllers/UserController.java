package com.TaskTracker.TaskTracker.controllers;

import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.teams.TeamNeverExistedException;
import com.TaskTracker.TaskTracker.exceptions.teams.UserNotInTeamException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/find/{email}")
    public ResponseEntity<?> getUserByEmail(String email){
        try {
            UserResponseDTO user = userService.userFindByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @GetMapping("/{teamName}/team-members")
    public ResponseEntity<?> getUsersInTeam(@PathVariable String teamName) {
        try {
            List<UserResponseDTO> userDtoList = userService.usersFindByTeam(teamName);
            return ResponseEntity.ok(userDtoList);
        } catch (TeamNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotInTeamException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/del/{email}")
    public ResponseEntity deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad delete request");
        }
    }
}
