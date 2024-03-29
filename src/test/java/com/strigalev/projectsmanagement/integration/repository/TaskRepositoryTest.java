package com.strigalev.projectsmanagement.integration.repository;

import com.strigalev.projectsmanagement.domain.Project;
import com.strigalev.projectsmanagement.domain.Task;
import com.strigalev.projectsmanagement.integration.IntegrationTestBase;
import com.strigalev.projectsmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskRepositoryTest extends IntegrationTestBase {
    private static final long ID = 1;
    private static final String TITLE = "Formation of requirements";

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testFindById() {
        Optional<Task> task = taskRepository.findById(ID);
        assertTrue(task.isPresent());
        assertEquals(TITLE, task.get().getTitle());
    }

    @Test
    void testFindAllByActiveIsTrue() {
        Page<Task> taskPage = taskRepository.findAllByProjectIdAndActiveIsTrue(PageRequest.of(0, 6), ID);
        assertThat(taskPage.getContent(), hasSize(5));
    }

    @Test
    void testFindAll() {
        Page<Task> taskPage = taskRepository.findAllByProjectId(PageRequest.of(0, 6), ID);
        assertThat(taskPage.getContent(), hasSize(6));
    }

}
