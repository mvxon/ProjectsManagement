package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

    boolean existsByName(String projectName);

    Page<Project> findAllByActiveIsTrue(Pageable pageable);

    List<Project> findAll();

    Optional<Project> findByName(String name);
}
