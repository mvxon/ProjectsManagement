package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.mapper.TaskMapper;
import com.strigalev.projectsmanagement.mapper.TasksListMapper;
import com.strigalev.projectsmanagement.repository.TaskRepository;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TasksListMapper tasksListMapper;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Override
    public List<TaskDTO> getAllTasksByProjectId(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return tasksListMapper.map(project.getTasks());
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task", id)
        );
    }

    @Override
    public TaskDTO getTaskDtoById(Long id) {
        return taskMapper.map(getTaskById(id));
    }

    private Task copyTask(Task task) {
        return Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate())
                .deadLineDate(task.getDeadLineDate())
                .active(task.isActive())
                .employees(task.getEmployees())
                .build();
    }

    @Override
    public boolean updateTask(TaskDTO taskDTO) {
        Task oldTask = taskRepository.findById(taskDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task", taskDTO.getId())
        );
        Task newTask = copyTask(oldTask);
        if (!Objects.equals(oldTask.getTitle(), taskDTO.getTitle())) {
            newTask.setTitle(taskDTO.getTitle());
        }
        if (!Objects.equals(oldTask.getDescription(), taskDTO.getDescription())) {
            newTask.setDescription(taskDTO.getDescription());
        }
        if (!Objects.equals(oldTask.getDeadLineDate(), LocalDate.parse(taskDTO.getDeadLineDate()))) {
            newTask.setDeadLineDate(LocalDate.parse(taskDTO.getDeadLineDate()));
        }
        if (!oldTask.equals(newTask)) {
            taskRepository.save(newTask);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Long createTask(TaskDTO taskDTO) {
        Task task = taskMapper.map(taskDTO);
        task.setCreationDate(LocalDate.now());
        task.setDeadLineDate(LocalDate.parse(taskDTO.getDeadLineDate()));
        task.setActive(true);
        taskRepository.save(task);
        return task.getId();
    }

    @Override
    @Transactional
    public boolean softDeleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task", id)
        );
        task.setActive(false);
        return true;
    }

    @Override
    public Page<TaskDTO> getProjectTasksPage(Pageable pageable, Long projectId) {
        Page<Task> tasks = taskRepository.findAllByProjectId(pageable, projectId);
        return tasks.map(tasksListMapper::map);
    }

    @Override
    public Page<TaskDTO> getProjectActiveTasksPage(Pageable pageable, Long projectId) {
        Page<Task> tasks = taskRepository.findAllByProjectIdAndActiveIsTrue(pageable, projectId);
        return tasks.map(tasksListMapper::map);
    }

}
