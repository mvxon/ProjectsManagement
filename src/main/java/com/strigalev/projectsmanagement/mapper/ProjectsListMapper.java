package com.strigalev.projectsmanagement.mapper;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectsListMapper {

    @Mappings({
            @Mapping(target = "title", ignore = true),
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "deadLineDate", ignore = true),
            @Mapping(target = "creationDate", ignore = true),

    })
    ProjectDTO map(Project project);

    List<ProjectDTO> map(List<Project> projects);

}
