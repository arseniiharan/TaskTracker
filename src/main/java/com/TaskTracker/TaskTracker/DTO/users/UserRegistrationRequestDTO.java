package com.TaskTracker.TaskTracker.DTO.users;

public record UserRegistrationRequestDTO(String password, String email, String name, String surname, String jobTitle, Boolean teamLeader) {
}
