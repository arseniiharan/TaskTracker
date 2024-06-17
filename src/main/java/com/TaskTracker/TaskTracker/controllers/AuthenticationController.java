package com.TaskTracker.TaskTracker.controllers;

import com.TaskTracker.TaskTracker.DTO.users.UserLoginRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserLoginResponseDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserRegistrationRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.exceptions.user.UserAlreadyExistsException;
import com.TaskTracker.TaskTracker.exceptions.user.UserNeverExistedException;
import com.TaskTracker.TaskTracker.security.AuthService;
import com.TaskTracker.TaskTracker.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
        try {
            UserResponseDTO userDto = userService.createUser(userRegistrationRequestDTO);
            return ResponseEntity.ok(userDto);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        return authService.authenticate(userLoginRequestDTO);
    }

    @PostMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        try {
            userService.activateUser(code);
            return ResponseEntity.ok("Email activated successfully");
        } catch (UserNeverExistedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad request");
        }
    }
}
