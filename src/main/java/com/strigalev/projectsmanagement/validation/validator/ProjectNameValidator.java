package com.strigalev.projectsmanagement.validation.validator;

import com.strigalev.projectsmanagement.service.ProjectService;
import com.strigalev.projectsmanagement.validation.annotation.ProjectName;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ProjectNameValidator implements ConstraintValidator<ProjectName, String> {
    private final ProjectService projectService;

    @Override
    public boolean isValid(String projectName, ConstraintValidatorContext constraintValidatorContext) {
        if (projectService.isProjectWithNameExists(projectName)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(String.format("Project with name %s already exists",
                            projectName))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
