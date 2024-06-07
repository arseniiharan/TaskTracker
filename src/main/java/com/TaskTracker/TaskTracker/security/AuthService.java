package com.TaskTracker.TaskTracker.security;


import com.TaskTracker.TaskTracker.DTO.users.UserLoginRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserLoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDTO.email(), userLoginRequestDTO.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDTO(token);
    }
}