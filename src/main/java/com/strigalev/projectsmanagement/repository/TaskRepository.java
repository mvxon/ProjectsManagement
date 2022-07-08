package com.strigalev.projectsmanagement.repository;

import com.strigalev.projectsmanagement.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {


    @Query(value = "SELECT * FROM tasks WHERE project_id = :id", nativeQuery = true)
    Page<Task> findAllByProjectIdAndActiveIsTrue(Pageable pageable, @Param("id") Long projectId);

    @Query(value = "SELECT * FROM tasks WHERE project_id = :id", nativeQuery = true)
    Page<Task> findAllByProjectId(Pageable pageable, @Param("id") Long projectId);

    @Modifying
    @Query(value = "UPDATE tasks SET active = false WHERE project_id = :id", nativeQuery = true)
    void setActiveFalseAllTasksByProjectId(@Param("id") Long projectId);
}
