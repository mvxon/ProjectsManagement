package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.mapper.TaskListMapper;
import com.strigalev.projectsmanagement.mapper.TaskMapper;
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

import static com.strigalev.projectsmanagement.util.MethodsUtil.getProjectNotExistsMessage;
import static com.strigalev.projectsmanagement.util.MethodsUtil.getTaskNotExistsMessage;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskListMapper taskListMapper;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Override
    public List<TaskDTO> getAllTasksByProjectId(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return taskListMapper.map(project.getTasks());
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(getTaskNotExistsMessage(id))
        );
    }

    @Override
    public TaskDTO getTaskDtoById(Long id) {
        return taskMapper.map(getTaskById(id));
    }

    @Override
    public void updateTask(TaskDTO taskDTO) {
        Task savedTask = getTaskById(taskDTO.getId());
        taskMapper.updateTaskFromDto(taskDTO, savedTask);
        taskRepository.save(savedTask);
    }

    @Override
    @Transactional
    public void softDeleteAllTasksByProjectId(Long projectId) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(getProjectNotExistsMessage(projectId));
        }
        taskRepository.setActiveFalseAllTasksByProjectId(projectId);
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
    public void softDeleteTask(Long id) {
        Task task = getTaskById(id);
        task.setActive(false);
        taskRepository.save(task);
    }

    @Override
    public Page<TaskDTO> getProjectTasksPage(Pageable pageable, Long projectId) {
        Page<Task> tasks = taskRepository.findAllByProjectId(pageable, projectId);
        return tasks.map(taskListMapper::map);
    }

    @Override
    public Page<TaskDTO> getProjectActiveTasksPage(Pageable pageable, Long projectId) {
        Page<Task> tasks = taskRepository.findAllByProjectIdAndActiveIsTrue(pageable, projectId);
        return tasks.map(taskListMapper::map);
    }

}
