package com.strigalev.projectsmanagement.controller;

import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
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
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("page")
    public ResponseEntity<?> getProjectsPage(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return new ResponseEntity<>(
                projectService.getProjectsPage(PageRequest.of(
                                pageNumber,
                                pageSize,
                                sortDir.equalsIgnoreCase("asc") ?
                                        Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
                        )
                ),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }


    @GetMapping("{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectDtoById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .objectId(projectService.createProject(projectDTO))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.softDeleteProject(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDTO) {
        ApiResponse apiResponse = new ApiResponse();
        projectDTO.setId(id);
        apiResponse.setMessage("updated : " + projectService.updateProject(projectDTO));
        return ResponseEntity.ok(apiResponse);
    }
}
