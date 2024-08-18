package com.TaskTracker.TaskTracker.mappers;

import com.TaskTracker.TaskTracker.DTO.users.UserRegistrationRequestDTO;
import com.TaskTracker.TaskTracker.DTO.users.UserResponseDTO;
import com.TaskTracker.TaskTracker.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target="email", source="dto.email")
    @Mapping(target="name", source="dto.name")
    @Mapping(target="surname", source="dto.surname")
    @Mapping(target="password", source="dto.password")
    @Mapping(target="jobTitle", source="dto.jobTitle")
    @Mapping(target="teamLeader", source="dto.teamLeader")
    User toModel(UserRegistrationRequestDTO dto);

    @Mapping(target="email", source="entity.email")
    @Mapping(target="name", source="entity.name")
    @Mapping(target="surname", source="entity.surname")
    @Mapping(target="jobTitle", source="entity.jobTitle")
    @Mapping(target="teamLeader", source="entity.teamLeader")
    UserResponseDTO toDto(User entity);
}
