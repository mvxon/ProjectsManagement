package com.strigalev.projectsmanagement.service;


import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    Long createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasksByProjectId(Long projectId);

    Task getTaskById(Long id);

    TaskDTO getTaskDtoById(Long id);

    void updateTask(TaskDTO taskDTO);

    void softDeleteAllTasksByProjectId(Long projectId);

    void softDeleteTask(Long id);

    Page<TaskDTO> getProjectTasksPage(Pageable pageable, Long projectId);

    Page<TaskDTO> getProjectActiveTasksPage(Pageable pageable, Long projectId);
}
