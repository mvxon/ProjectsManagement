package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.mapper.ProjectMapper;
import com.strigalev.projectsmanagement.mapper.ProjectsListMapper;
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
import java.util.Objects;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectsListMapper projectsListMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectMapper projectMapper,
                              ProjectsListMapper projectsListMapper,
                              @Lazy TaskService taskService
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectsListMapper = projectsListMapper;
        this.taskService = taskService;
    }

    @Override
    @Transactional
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Project with %oid does not exists", id))
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
        projectRepository.findAll();
        return projectsListMapper.map(projectRepository.findAll());
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
    public boolean isProjectWithNameExists(String projectName) {
        return projectRepository.existsByName(projectName);
    }

    private Project copyProject(Project project) {
        return Project.builder()
                .id(project.getId())
                .name(project.getName())
                .creationDate(project.getCreationDate())
                .customer(project.getCustomer())
                .deadLineDate(project.getDeadLineDate())
                .description(project.getDescription())
                .title(project.getTitle())
                .employees(project.getEmployees())
                .tasks(project.getTasks())
                .active(project.isActive())
                .build();
    }

    @Override
    @Transactional
    public boolean updateProject(ProjectDTO projectDTO) {
        Project oldProject = projectRepository.findById(projectDTO.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Project with %oid does not exists",
                                projectDTO.getId()))
                );
        Project newProject = copyProject(oldProject);
        if (!Objects.equals(oldProject.getName(), projectDTO.getName())) {
            newProject.setName(projectDTO.getName());
        }
        if (!Objects.equals(oldProject.getTitle(), projectDTO.getTitle())) {
            newProject.setTitle(projectDTO.getTitle());
        }
        if (!Objects.equals(oldProject.getDescription(), projectDTO.getDescription())) {
            newProject.setDescription(projectDTO.getDescription());
        }
        if (!Objects.equals(oldProject.getCustomer(), projectDTO.getCustomer())) {
            newProject.setCustomer(projectDTO.getCustomer());
        }
        if (!Objects.equals(oldProject.getDeadLineDate(), LocalDate.parse(projectDTO.getDeadLineDate()))) {
            newProject.setDeadLineDate(LocalDate.parse(projectDTO.getDeadLineDate()));
        }
        if (!Objects.equals(oldProject, newProject)) {
            projectRepository.save(newProject);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean softDeleteProject(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (Objects.isNull(project)) {
            throw new ResourceNotFoundException(String.format("Project with %oid does not exists", id));
        }
        project.setActive(false);
        projectRepository.save(project);
        return true;
    }

    @Override
    public boolean addTaskToProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Project with %oid does not exists",
                                projectId))
                );
        project.getTasks().add(taskService.getTaskById(taskId));
        return true;
    }

    @Override
    public Page<ProjectDTO> getProjectsPage(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(projectsListMapper::map);
    }
}
