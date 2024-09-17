package com.microservice.entertainment.exception;

import com.microservice.entertainment.models.enums.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorType errorType;
    private final HttpStatus status;

    public CustomException(ErrorType errorType, String message, HttpStatus status) {
        super(message);
        this.errorType = errorType;
        this.status = status;
    }

}
