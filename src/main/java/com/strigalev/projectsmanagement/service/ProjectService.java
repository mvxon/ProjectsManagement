package com.strigalev.projectsmanagement.service;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

public interface ProjectService {

    Project getProjectById(Long id);

    Map<String, ?> createProject(ProjectDTO projectDTO, BindingResult bindingResult);

    List<ProjectDTO> getAllProjects();

    void deleteProject(Long id);

    boolean isProjectWithIdExists(Long id);

    boolean isProjectWithNameExists(String projectName);

    Map<String, ?> updateProject(ProjectDTO projectDTO, BindingResult bindingResult);

    Map<String, ?> softDeleteProject(Long id);

    Map<String, ?> addTaskToProject(Long projectId, Long taskId);


}
