package com.lingxun.smartgunchecksystem.exception;

import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", errors);
        return new ResponseEntity<>(UnifiedResponse.error(ResponseCode.VALIDATION_ERROR, errors), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleBindException(BindException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Binding error: {}", errors);
        return new ResponseEntity<>(UnifiedResponse.error(ResponseCode.VALIDATION_ERROR, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(UnifiedResponse.error(ResponseCode.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    // Custom application exception (example)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleAppException(AppException ex) {
        log.error("Application exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(UnifiedResponse.error(ex.getResponseCode(), ex.getMessage()), HttpStatus.valueOf(ex.getResponseCode().getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UnifiedResponse<Object>> handleAllExceptions(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(UnifiedResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
