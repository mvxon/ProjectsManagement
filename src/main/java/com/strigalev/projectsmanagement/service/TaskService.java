package com.strigalev.projectsmanagement.service;


import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;

import java.util.List;

public interface TaskService extends PageService<TaskDTO> {
    Long createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasksByProjectId(Long projectId);

    Task getTaskById(Long id);

    boolean updateTask(TaskDTO taskDTO);

    boolean softDeleteTask(Long id);
}
