package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping("{projectId}")
    public ResponseEntity<?> getAllProjectTasks(@PathVariable Long projectId) {
        Map<String, String> response = new LinkedHashMap<>();

        if (!projectService.isProjectWithIdExists(projectId)) {
            response.put("error", String.format("Project with %oid don't exists", projectId));
            return ResponseEntity.badRequest().body(response);
        }

        List<TaskDTO> tasks = taskService.getAllTasksByProjectId(projectId);
        if (tasks.isEmpty()) {
            response.put("error", String.format("Project with %oid don't have tasks", projectId));
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("{projectId}")
    public ResponseEntity<?> createTaskInProject(@PathVariable Long projectId,
                                                 @RequestBody @Valid TaskDTO taskDTO,
                                                 BindingResult bindingResult
    ) {
        // переделать, сначала проверка на существование проекта
        Map<String, Object> response = new LinkedHashMap<>(taskService.createTask(taskDTO, bindingResult));
        if (response.get("created") == Boolean.FALSE) {
            return ResponseEntity.badRequest().body(response);
        }
        Long id = (Long) taskService.createTask(taskDTO, bindingResult).get("id");
        response.putAll(projectService.addTaskToProject(projectId, id));
        if (response.get("added") == Boolean.FALSE) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        Map<String, Object> response = new LinkedHashMap<>(taskService.softDeleteTask(taskId));
        if (response.get("deleted") == Boolean.FALSE) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>(taskService.updateTask(taskDTO, bindingResult));
        if (response.get("changed") == Boolean.TRUE) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
