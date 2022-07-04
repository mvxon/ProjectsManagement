package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

    boolean existsByName(String projectName);

    List<Project> findAll();
}
