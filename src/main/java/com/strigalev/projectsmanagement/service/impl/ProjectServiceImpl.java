package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.mapper.ProjectMapper;
import com.strigalev.projectsmanagement.mapper.ProjectListMapper;
import com.strigalev.projectsmanagement.repository.ProjectRepository;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.strigalev.projectsmanagement.util.MethodsUtil.getProjectNotExistsMessage;


@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectListMapper projectListMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectMapper projectMapper,
                              ProjectListMapper projectListMapper,
                              @Lazy TaskService taskService
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectListMapper = projectListMapper;
        this.taskService = taskService;
    }

    @Override
    @Transactional
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(getProjectNotExistsMessage(id))
                );
    }

    @Override
    public ProjectDTO getProjectDtoById(Long id) {
        return projectMapper.map(getProjectById(id));
    }

    @Override
    @Transactional
    public Long createProject(ProjectDTO projectDTO) {
        Project project = projectMapper.map(projectDTO);
        project.setCreationDate(LocalDate.now());
        project.setDeadLineDate(LocalDate.parse(projectDTO.getDeadLineDate()));
        project.setActive(true);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    @Transactional
    public List<ProjectDTO> getAllProjects() {
        return projectListMapper.map(projectRepository.findAll());
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean isProjectWithIdExists(Long id) {
        return projectRepository.existsById(id);
    }

    @Override
    @Transactional
    public boolean isProjectWithNameExists(String projectName) {
        return projectRepository.existsByName(projectName);
    }

    @Override
    @Transactional
    public void updateProject(ProjectDTO projectDTO) {
        Project savedProject = getProjectById(projectDTO.getId());
        projectMapper.updateProjectFromDto(projectDTO, savedProject);
        projectRepository.save(savedProject);
    }

    @Override
    @Transactional
    public void softDeleteProject(Long id) {
        Project project = getProjectById(id);
        project.setActive(false);
        taskService.softDeleteAllTasksByProjectId(id);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void addTaskToProject(Long projectId, Long taskId) {
        Project project = getProjectById(projectId);
        project.getTasks().add(taskService.getTaskById(taskId));
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public Page<ProjectDTO> getProjectsPage(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(projectListMapper::map);
    }

    @Override
    @Transactional
    public Page<ProjectDTO> getActiveProjectsPage(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByActiveIsTrue(pageable);
        return projects.map(projectListMapper::map);
    }
}
