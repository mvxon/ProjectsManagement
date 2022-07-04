package com.strigalev.projectsmanagement.mapper;

import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TasksListMapper {

    @Mappings({
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "deadLineDate", ignore = true),
            @Mapping(target = "creationDate", ignore = true)
    })
    TaskDTO map(Task task);

    List<TaskDTO> map(List<Task> tasks);

}
