package com.strigalev.projectsmanagement.service;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Project getProjectById(Long id);

    ProjectDTO getProjectDtoById(Long id);

    Long createProject(ProjectDTO projectDTO);

    List<ProjectDTO> getAllProjects();

    void deleteProject(Long id);

    boolean isProjectWithIdExists(Long id);

    boolean isProjectWithNameExists(String projectName);

    boolean updateProject(ProjectDTO projectDTO);

    boolean softDeleteProject(Long id);

    boolean addTaskToProject(Long projectId, Long taskId);

    Page<ProjectDTO> getProjectsPage(Pageable pageable);

}
