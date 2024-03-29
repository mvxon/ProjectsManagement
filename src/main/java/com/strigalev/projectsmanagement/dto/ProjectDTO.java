package com.strigalev.projectsmanagement.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.strigalev.projectsmanagement.validation.annotation.Date;
import com.strigalev.projectsmanagement.validation.annotation.ProjectName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

    private Long id;

    @ProjectName
    @NotEmpty(message = "Name should not be empty")
    @Size(max = 30, min = 3, message = "Name length should be between {min} and {max} chars")
    private String name;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 7, max = 40, message = "Title length should be between {min} and {max} chars")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @NotEmpty(message = "Customer should not be empty")
    @Size(min = 3, max = 30, message = "Customer length should be between {min} and {max} chars")
    private String customer;

    @NotEmpty(message = "Description should not be empty")
    @Size(min = 10, max = 1000, message = "Description length should be between {min} and {max} chars")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String creationDate;

    @NotEmpty(message = "Deadline date should not be empty")
    @Date
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deadLineDate;
}
