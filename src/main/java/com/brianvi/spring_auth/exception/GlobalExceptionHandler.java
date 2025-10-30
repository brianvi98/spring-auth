package com.brianvi.spring_auth.exception;

import com.brianvi.spring_auth.response.ApiResponse;
import com.brianvi.spring_auth.security.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException exception, HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception, HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI()
                ));
    }
}
