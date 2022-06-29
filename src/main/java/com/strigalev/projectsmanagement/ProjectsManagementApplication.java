package com.strigalev.projectsmanagement;


import com.strigalev.projectsmanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectsManagementApplication implements CommandLineRunner {
    @Autowired
    ProjectService projectService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectsManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
