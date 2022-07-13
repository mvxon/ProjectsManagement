package com.strigalev.projectsmanagement.mapper;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO map(Project project);

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "id", ignore = true)
    Project map(ProjectDTO projectDTO);

    @Mappings({
            @Mapping(target = "tasks", ignore = true),
            @Mapping(target = "employees", ignore = true),
            @Mapping(target = "active", ignore = true)
    })
    void updateProjectFromDto(ProjectDTO projectDTO, @MappingTarget Project project);
}
