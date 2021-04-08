package com.dryxtech.software.sample.controller;

import com.dryxtech.software.sample.exception.InvalidSearchException;
import com.dryxtech.software.sample.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidSearchException.class})
    protected ResponseEntity<Object> handleInvalidSearchException(InvalidSearchException ex) {
        String message = "Invalid Search Error";
        log.warn("{}: {}", message, ex.getMessageWithContext());

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(message);
        if (Objects.nonNull(ex.getCause())) {
            apiError.setDebugMessage(ex.getCause().getLocalizedMessage());
        } else {
            apiError.setDebugMessage(ex.getLocalizedMessage());
        }

        Map<String, Object> context = ex.getContext().orElse(Collections.emptyMap());
        for (Object value : context.values()) {
            if (value instanceof Errors) {
                Errors errors = (Errors) value;
                apiError.addValidationErrors(errors.getFieldErrors());
                apiError.addValidationError(errors.getGlobalErrors());
            }
        }

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String error = "Invalid Request Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(IllegalStateException ex) {
        String error = "Invalid State Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(CONFLICT, error, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        String error = "Entity Not Found Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(NOT_FOUND, error, ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            String error = "Database Integrity Error";
            log.error(error, ex);
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, error, ex.getCause()));
        }
        return handleException(ex);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        String error = "General Processing Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, error, ex));
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessViolation(DataAccessException ex) {
        String error = "Data Access Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, error, ex.getCause()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String error = "Malformed JSON Request Error";
        log.error(error, ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
}
