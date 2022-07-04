package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.service.PageService;
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
    private final PageService<TaskDTO> pageService;

    @GetMapping("{projectId}")
    public ResponseEntity<?> getAllProjectTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasksByProjectId(projectId));
    }

    @GetMapping("{projectId}/page")
    public ResponseEntity<?> getTasksPage(
            @PathVariable Long projectId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(String.format("Project with %oid does not exists", projectId));
        }
        return new ResponseEntity<>(pageService.getPage(
                PageRequest.of(
                        pageNumber, pageSize,
                        sortDir.equalsIgnoreCase("asc") ?
                                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
                )
        ), HttpStatus.OK);
    }

    @PostMapping("{projectId}")
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId, @RequestBody @Valid TaskDTO taskDTO
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(String.format("Project with %oid does not exists", projectId));
        }
        ApiResponse apiResponse = new ApiResponse();
        Long taskId = taskService.createTask(taskDTO);
        if (projectService.addTaskToProject(projectId, taskId)) {
            apiResponse.setObjectId(taskId);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.softDeleteTask(taskId));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        ApiResponse apiResponse = new ApiResponse();
        taskDTO.setId(id);
        apiResponse.setMessage("updated : " + taskService.updateTask(taskDTO));
        return ResponseEntity.ok(apiResponse);
    }
}
