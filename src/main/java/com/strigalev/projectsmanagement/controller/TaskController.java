package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import com.strigalev.projectsmanagement.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.strigalev.projectsmanagement.util.MethodsUtil.getProjectNotExistsMessage;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping("{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksPage(@PathVariable Long projectId, Pageable pageable) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(getProjectNotExistsMessage(projectId));
        }
        return new ResponseEntity<>(pageable, OK);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId, @RequestBody @Valid TaskDTO taskDTO
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(getProjectNotExistsMessage(projectId));
        }
        Long taskId = taskService.createTask(taskDTO);
        projectService.addTaskToProject(projectId, taskId);
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .objectId(taskId)
                        .build(),
                CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.softDeleteTask(id);
        return ResponseEntity.ok(new ApiResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        taskDTO.setId(id);
        taskService.updateTask(taskDTO);
        return ResponseEntity.ok(new ApiResponse());
    }
}
