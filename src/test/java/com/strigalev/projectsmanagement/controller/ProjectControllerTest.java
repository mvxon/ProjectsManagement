package com.strigalev.projectsmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strigalev.projectsmanagement.dto.ProjectDTO;
import com.strigalev.projectsmanagement.service.ProjectService;
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
class ProjectControllerTest {
    private static final String URL = "/api/v1/projects";
    private static final long ID = 1L;
    private static final String NAME = "ProjectX";
    private static final String TITLE = "Asd asd asd";
    private static final String DESC = "Asd asd asd asd asd";
    private static final String CUSTOMER = "FEAR";
    private static final String DEAD_LINE_DATE = "2025-12-15";
    private static final String CREATION_DATE = "2022-07-7";
    private static final ProjectDTO project = ProjectDTO.builder()
            .name(NAME)
            .title(TITLE)
            .description(DESC)
            .id(ID)
            .customer(CUSTOMER)
            .deadLineDate(DEAD_LINE_DATE)
            .creationDate(CREATION_DATE)
            .build();

    @MockBean
    private ProjectService projectService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProjectById() throws Exception {
        when(projectService.getProjectDtoById(ID)).thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TITLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(DESC))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer").value(CUSTOMER))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadLineDate").value(DEAD_LINE_DATE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(CREATION_DATE));

    }

    @Test
    void createProject() throws Exception {
        when(projectService.createProject(project)).thenReturn(ID);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(new ObjectMapper().writeValueAsString(project))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value(ID));
    }

    @Test
    void createProject_whenNotValid() throws Exception {
        when(projectService.createProject(project)).thenReturn(ID);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(new ObjectMapper().writeValueAsString(new ProjectDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + ID))
                .andExpect(status().isOk());
        verify(projectService).softDeleteProject(ID);
    }


    @Test
    void updateProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + ID)
                        .content(new ObjectMapper().writeValueAsString(project))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(projectService).updateProject(project);
    }

}