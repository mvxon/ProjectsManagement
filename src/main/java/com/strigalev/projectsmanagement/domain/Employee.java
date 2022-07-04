package com.strigalev.projectsmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @ManyToMany(mappedBy = "employees")
    @Where(clause = "active")
    private Set<Project> workingProjects;
    @ManyToMany(mappedBy = "employees")
    private Set<Task> workingTasks;
}
