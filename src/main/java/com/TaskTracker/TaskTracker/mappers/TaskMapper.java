package com.TaskTracker.TaskTracker.mappers;

import com.TaskTracker.TaskTracker.DTO.tasks.TaskDTO;
import com.TaskTracker.TaskTracker.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target="taskName", source="dto.taskName")
    @Mapping(target="startDate", source="dto.startDate")
    @Mapping(target="endDate", source="dto.endDate")
    @Mapping(target="taskImportance", source="dto.taskImportance")
    Task toModel(TaskDTO dto);

    @Mapping(target="taskName", source="entity.taskName")
    @Mapping(target="startDate", source="entity.startDate")
    @Mapping(target="endDate", source="entity.endDate")
    @Mapping(target="taskImportance", source="entity.taskImportance")
    TaskDTO toDto(Task entity);
}
