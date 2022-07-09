package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getProjectsPage(Pageable pageable) {
        return new ResponseEntity<>(projectService.getActiveProjectsPage(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectDtoById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .objectId(projectService.createProject(projectDTO))
                        .build(),
                CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.softDeleteProject(id);
        return ResponseEntity.ok(new ApiResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDTO) {
        projectDTO.setId(id);
        projectService.updateProject(projectDTO);
        return ResponseEntity.ok(new ApiResponse());
    }
}
