package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import com.strigalev.projectsmanagement.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<?> getTasksPage(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "creation_date") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }
        return new ResponseEntity<>(taskService.getProjectActiveTasksPage(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        sortDir.equalsIgnoreCase("asc") ?
                                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
                ), projectId
        ), HttpStatus.OK);
    }

    @PostMapping("{projectId}")
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId, @RequestBody @Valid TaskDTO taskDTO
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException("Project", projectId);
        }
        ApiResponse apiResponse = new ApiResponse();
        Long taskId = taskService.createTask(taskDTO);
        if (projectService.addTaskToProject(projectId, taskId)) {
            apiResponse.setObjectId(taskId);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.softDeleteTask(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        ApiResponse apiResponse = new ApiResponse();
        taskDTO.setId(id);
        apiResponse.setMessage("updated : " + taskService.updateTask(taskDTO));
        return ResponseEntity.ok(apiResponse);
    }
}
