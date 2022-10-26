package com.strigalev.projectsmanagement.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@UtilityClass
public class MethodsUtil {
    public String getBindingResultErrors(@NotNull BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        }
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (bindingResult.getFieldErrors().indexOf(fieldError) == bindingResult.getFieldErrors().size() - 1) {
                errors.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage());
                return errors.toString();
            }
            errors.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append(", ");
        }
        return errors.toString();
    }

    public String getProjectNotExistsMessage(Long projectId) {
        return String.format("Project with %oid does not exists", projectId);
    }

    public String getTaskNotExistsMessage(Long taskId) {
        return String.format("Task with %oid does not exists", taskId);
    }

}
