package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
}
