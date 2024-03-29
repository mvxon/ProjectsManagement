package com.strigalev.projectsmanagement.mapper;

import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO map(Task task);

    List<TaskDTO> map(List<Task> tasks);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "active", ignore = true)
    Task map(TaskDTO taskDTO);

    @Mappings({
            @Mapping(target = "employees", ignore = true),
            @Mapping(target = "active", ignore = true)
    })
    void updateTaskFromDto(TaskDTO taskDTO, @MappingTarget Task task);
}

