package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByName(String projectName);

    boolean existsByName(String projectName);
}
