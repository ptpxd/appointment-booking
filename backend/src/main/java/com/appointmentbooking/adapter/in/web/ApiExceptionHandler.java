package com.appointmentbooking.adapter.in.web;

import com.appointmentbooking.application.ApiException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiErrorResponse> apiException(ApiException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new ApiErrorResponse(exception.getCode(), exception.getMessage(), Map.of(), exception.getDetails()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> validation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> { if (error instanceof FieldError fieldError) fields.put(fieldError.getField(), fieldError.getDefaultMessage()); });
        return ResponseEntity.badRequest().body(new ApiErrorResponse("VALIDATION_ERROR", "One or more fields are invalid.", fields, Map.of()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiErrorResponse> illegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiErrorResponse("INVALID_REQUEST", exception.getMessage(), Map.of(), Map.of()));
    }
}