package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.mapper.ProjectMapper;
import com.strigalev.projectsmanagement.mapper.ProjectsListMapper;
import com.strigalev.projectsmanagement.repository.ProjectRepository;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import com.strigalev.projectsmanagement.validation.GetErrorsAction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectsListMapper projectsListMapper;
    private final TaskService taskService;
    private final GetErrorsAction getErrorsAction;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ProjectMapper projectMapper,
                              ProjectsListMapper projectsListMapper,
                              @Lazy TaskService taskService,
                              GetErrorsAction getErrorsAction) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectsListMapper = projectsListMapper;
        this.taskService = taskService;
        this.getErrorsAction = getErrorsAction;
    }

    @Override
    @Transactional
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Map<String, ?> createProject(ProjectDTO projectDTO, BindingResult bindingResult) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "created";

        if (bindingResult.hasErrors()) {
            result.put(message, Boolean.FALSE);
            result.putAll(getErrorsAction.execute(bindingResult));
            return result;
        }

        Project project = projectMapper.map(projectDTO);
        project.setCreationDate(LocalDate.now());
        project.setDeadLineDate(LocalDate.parse(projectDTO.getDeadLineDate()));
        project.setActive(true);
        projectRepository.save(project);
        result.put(message, Boolean.TRUE);
        result.put("id", project.getId());
        return result;
    }

    @Override
    @Transactional
    public List<ProjectDTO> getAllProjects() {
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
    public Map<String, ?> updateProject(ProjectDTO projectDTO, BindingResult bindingResult) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "changed";
        result.put(message, Boolean.FALSE);
        Project oldProject = projectRepository.findById(projectDTO.getId()).orElse(null);
        if (oldProject == null) {
            result.put("error", "not exists");
            return result;
        }
        if (bindingResult.hasErrors()) {
            result.putAll(getErrorsAction.execute(bindingResult));
            return result;
        }
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
            newProject.setDescription(projectDTO.getCustomer());
        }
        if (!Objects.equals(oldProject.getCustomer(), projectDTO.getCustomer())) {
            newProject.setDescription(projectDTO.getCustomer());
        }
        if (!Objects.equals(oldProject.getDeadLineDate(), LocalDate.parse(projectDTO.getDeadLineDate()))) {
            newProject.setDeadLineDate(LocalDate.parse(projectDTO.getDeadLineDate()));
        }
        if (!oldProject.equals(newProject)) {
            projectRepository.save(newProject);
            result.put(message, Boolean.TRUE);
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, ?> softDeleteProject(Long id) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "deleted";
        result.put(message, Boolean.FALSE);
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            result.put("error:", String.format("Project with %oid does not exists", id));
            return result;
        }
        result.put(message, Boolean.TRUE);
        project.setActive(false);
        projectRepository.save(project);
        return result;
    }

    @Override
    public Map<String, ?> addTaskToProject(Long projectId, Long taskId) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "added";
        result.put(message, Boolean.FALSE);
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            result.put("error:", String.format("Project with %oid does not exists", projectId));
            return result;
        }
        project.getTasks().add(taskService.getTaskById(taskId));
        result.put(message, Boolean.TRUE);
        return result;
    }


}
