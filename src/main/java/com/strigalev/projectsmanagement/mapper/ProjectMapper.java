package com.strigalev.projectsmanagement.mapper;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;


@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO map(Project project);

    List<ProjectDTO> map(List<Project> projects);

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Project map(ProjectDTO projectDTO);
}
