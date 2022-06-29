package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        if (!projectService.isProjectWithIdExists(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("error", String.format("Project with %oid does not exists", id));
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>(projectService.createProject(projectDTO, bindingResult));
        if (response.get("created") == Boolean.TRUE) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>(projectService.softDeleteProject(id));
        if (response.get("deleted") == Boolean.TRUE) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>(projectService.updateProject(projectDTO, bindingResult));
        if (response.get("changed") == Boolean.TRUE) {
            ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
