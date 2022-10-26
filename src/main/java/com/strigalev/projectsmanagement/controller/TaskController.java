package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.ApiResponseEntity;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.exception.ResourceNotFoundException;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.strigalev.projectsmanagement.util.MethodsUtil.getProjectNotExistsMessage;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Task", description = "Endpoints for tasks managing")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping("{id}")
    @Operation(summary = "Get task by id", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SUCCESS",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content),
            @ApiResponse(responseCode = "500", description = "INTERNAL ERROR", content = @Content)
    })
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get project tasks page", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SUCCESS",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content),
            @ApiResponse(responseCode = "500", description = "INTERNAL ERROR", content = @Content)
    })
    public ResponseEntity<?> getProjectTasksPage(@PathVariable Long projectId, Pageable pageable) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(getProjectNotExistsMessage(projectId));
        }
        return ResponseEntity.ok(taskService.getProjectActiveTasksPage(pageable, projectId));
    }

    @PostMapping("/{projectId}")
    @Operation(summary = "Create task in project",
            responses = {
            @ApiResponse(responseCode = "201",
                    description = "SUCCESSFULLY CREATED",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content),
            @ApiResponse(responseCode = "500", description = "INTERNAL ERROR", content = @Content)
    })
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId, @RequestBody @Valid TaskDTO taskDTO
    ) {
        if (!projectService.isProjectWithIdExists(projectId)) {
            throw new ResourceNotFoundException(getProjectNotExistsMessage(projectId));
        }
        Long taskId = taskService.createTask(taskDTO);
        projectService.addTaskToProject(projectId, taskId);
        return new ResponseEntity<>(
                ApiResponseEntity.builder()
                        .objectId(taskId)
                        .build(),
                CREATED
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete task by id", responses = {
            @ApiResponse(responseCode = "200", description = "SUCCESS"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL ERROR")
    })
    public void deleteTask(@PathVariable Long id) {
        taskService.softDeleteTask(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update task by id", responses = {
            @ApiResponse(responseCode = "200", description = "SUCCESS"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL ERROR")
    })
    public void updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        taskDTO.setId(id);
        taskService.updateTask(taskDTO);
    }
}
