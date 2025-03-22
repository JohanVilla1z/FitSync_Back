package com.johan.gym_control.exceptions;

import com.johan.gym_control.exceptions.auth.AuthenticationException;
import com.johan.gym_control.models.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  // Add more exception handlers as needed
}