package com.strigalev.projectsmanagement.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class MethodsUtil {
    public static String getBindingResultErrors(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
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

    public static String getProjectNotExistsMessage(Long projectId) {
        return String.format("Project with %oid does not exists", projectId);
    }

    public static String getTaskNotExistsMessage(Long taskId) {
        return String.format("Task with %oid does not exists", taskId);
    }

}
