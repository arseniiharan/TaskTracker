package com.TaskTracker.TaskTracker.mappers;

import com.TaskTracker.TaskTracker.DTO.teams.TeamDTO;
import com.TaskTracker.TaskTracker.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    @Mapping(target = "teamName", source = "dto.teamName")
    Team toModel(TeamDTO dto);

    @Mapping(target="teamName", source = "entity.teamName")
    TeamDTO toDto(Team entity);
}
