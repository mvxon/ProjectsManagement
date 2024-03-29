package com.strigalev.projectsmanagement.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strigalev.projectsmanagement.dto.TaskDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.strigalev.projectsmanagement.util.MethodsUtil.getProjectNotExistsMessage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    private static final String PATH = "/api/v1/tasks";
    private static final Long ID = 1L;
    private static final String TITLE = "Asd asd asd";
    private static final String DESC = "Asd asd asd asd asd";
    private static final String DEAD_LINE_DATE = "2025-12-15";
    private static final String CREATION_DATE = "2022-07-7";
    private static final TaskDTO TASK_DTO = TaskDTO.builder()
            .id(ID)
            .title(TITLE)
            .deadLineDate(DEAD_LINE_DATE)
            .description(DESC)
            .creationDate(CREATION_DATE)
            .build();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @MockBean
    private ProjectService projectService;

    @Test
    void getTaskById() throws Exception {
        when(taskService.getTaskDtoById(ID)).thenReturn(TASK_DTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/" + ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TITLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(DESC))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadLineDate").value(DEAD_LINE_DATE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(CREATION_DATE));
    }

    @Test
    void createTaskInProject() throws Exception {
        when(projectService.isProjectWithIdExists(ID)).thenReturn(true);
        when(taskService.createTask(TASK_DTO)).thenReturn(ID);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(TASK_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value(ID));
        verify(projectService).addTaskToProject(ID, ID);
    }

    @Test
    void createTaskInProject_whenProjectNotExists() throws Exception {
        when(projectService.isProjectWithIdExists(ID)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(TASK_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(getProjectNotExistsMessage(ID)));
    }

    @Test
    void createTask_whenInvalidRequestBody() throws Exception {
        when(taskService.createTask(TASK_DTO)).thenReturn(ID);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(new TaskDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(status().isBadRequest());
    }


    @Test
    void getProjectTasksPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/" + ID))
                .andExpect(status().isOk());
    }


    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/" + ID))
                .andExpect(status().isOk());
        verify(taskService).softDeleteTask(ID);
    }

    @Test
    void updateTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(TASK_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(taskService).updateTask(TASK_DTO);
    }

    @Test
    void updateTask_whenInvalidRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(new TaskDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(status().isBadRequest());
    }

}