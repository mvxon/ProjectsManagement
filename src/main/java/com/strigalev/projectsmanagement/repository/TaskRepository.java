package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
