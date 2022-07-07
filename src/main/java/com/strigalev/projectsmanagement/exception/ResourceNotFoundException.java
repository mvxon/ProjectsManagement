package com.strigalev.projectsmanagement.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format(resourceName + " with %oid does not exists", id));
    }
}
