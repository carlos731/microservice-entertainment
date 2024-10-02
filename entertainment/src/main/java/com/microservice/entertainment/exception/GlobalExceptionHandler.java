package com.microservice.entertainment.exception;

import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toEpochMilli(),
                ex.getErrorType().getStatusCode(),
                ex.getErrorType().getDescription(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationErrors(MethodArgumentNotValidException ex, HttpServletRequest request){

        ValidationError errors = new ValidationError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                "Error in the validation of the fields",
                request.getRequestURI()
        );

        for(FieldError x : ex.getBindingResult().getFieldErrors()) {
            errors.addError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toEpochMilli(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorType.FOREIGN_KEY_VIOLATION.getDescription(),
                "Violação de chave estrangeira: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Unauthorized
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> uns(LockedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toEpochMilli(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Você não está autenticado.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Exception handler para erro 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now().toEpochMilli(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You do not have permission to access this feature.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

// erro intertno 500
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                Instant.now().toEpochMilli(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Internal Server Error",
//                "Ocorreu um erro interno",
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
