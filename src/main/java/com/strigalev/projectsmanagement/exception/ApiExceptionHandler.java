package com.strigalev.projectsmanagement.exception;

import com.strigalev.projectsmanagement.util.GetErrorsAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final GetErrorsAction getErrorsAction;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return ResponseEntity.badRequest().body(ApiException.builder()
                .errorCode(ex.getMessage())
                .status(status)
                .message(getErrorsAction.execute(ex.getBindingResult()))
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        return new ResponseEntity<>(ApiException.builder()
                .errorCode(ex.getMessage())
                .status(status)
                .build(), status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(ApiException.builder()
                .message(ex.getMessage())
                .status(status)
                .build(), status);
    }
}
