package com.strigalev.projectsmanagement.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class GetErrorsAction {
    public String execute(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        String errors = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (bindingResult.getFieldErrors().indexOf(fieldError) == bindingResult.getFieldErrors().size()-1) {
                errors += fieldError.getField() + " : " + fieldError.getDefaultMessage();
                return errors;
            }
            errors += fieldError.getField() + " : " + fieldError.getDefaultMessage() + ", ";
        }
        return errors;
    }
}
