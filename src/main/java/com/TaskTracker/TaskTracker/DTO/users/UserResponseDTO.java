package com.TaskTracker.TaskTracker.DTO.users;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String email;
    private String name;
    private String surname;
    private String jobTitle;
    private boolean teamLeader;
}
