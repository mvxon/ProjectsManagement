package com.strigalev.projectsmanagement.service;


import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Map<String, ?> createTask(TaskDTO taskDTO, BindingResult bindingResult);

    List<TaskDTO> getAllTasksByProjectId(Long projectId);

    Task getTaskById(Long id);

    Map<String, ?> updateTask(TaskDTO taskDTO, BindingResult bindingResult);

    Map<String, ?> softDeleteTask(Long taskId);
}
