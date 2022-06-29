package com.strigalev.projectsmanagement.service.impl;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.mapper.TaskMapper;
import com.strigalev.projectsmanagement.repository.TaskRepository;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import com.strigalev.projectsmanagement.validation.GetErrorsAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final GetErrorsAction getErrorsAction;

    @Override
    public List<TaskDTO> getAllTasksByProjectId(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            return null;
        }
        return taskMapper.map(project.getTasks());
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
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
    public Map<String, ?> updateTask(TaskDTO taskDTO, BindingResult bindingResult) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "changed";
        result.put(message, Boolean.FALSE);
        Task oldTask = taskRepository.findById(taskDTO.getId()).orElse(null);
        if (oldTask == null) {
            result.put("error", String.format("Task with %oid does not exists", taskDTO.getId()));
            return result;
        }
        if (bindingResult.hasErrors()) {
            result.putAll(getErrorsAction.execute(bindingResult));
            return result;
        }
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
            result.put(message, Boolean.TRUE);
            taskRepository.save(newTask);
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, ?> createTask(TaskDTO taskDTO, BindingResult bindingResult) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "created";

        if (bindingResult.hasErrors()) {
            result.put(message, Boolean.FALSE);
            result.putAll(getErrorsAction.execute(bindingResult));
            return result;
        }

        Task task = taskMapper.map(taskDTO);
        task.setCreationDate(LocalDate.now());
        task.setDeadLineDate(LocalDate.parse(taskDTO.getDeadLineDate()));
        task.setActive(true);
        taskRepository.save(task);
        result.put(message, Boolean.TRUE);
        result.put("id", task.getId());
        return result;
    }

    @Override
    @Transactional
    public Map<String, ?> softDeleteTask(Long taskId) {
        Map<String, Object> result = new LinkedHashMap<>();
        String message = "deleted";
        result.put(message, Boolean.FALSE);
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            result.put("error:", String.format("Task with %oid does not exists", taskId));
            return result;
        }
        task.setActive(false);
        taskRepository.save(task);
        result.put(message, Boolean.TRUE);
        return result;
    }
}
