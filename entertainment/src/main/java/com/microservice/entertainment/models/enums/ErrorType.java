package com.microservice.entertainment.models.enums;


import lombok.Getter;

@Getter
public enum ErrorType {
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    FOREIGN_KEY_VIOLATION(400, "Foreign Key Violation");

    private final int statusCode;
    private final String description;

    ErrorType(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

}
