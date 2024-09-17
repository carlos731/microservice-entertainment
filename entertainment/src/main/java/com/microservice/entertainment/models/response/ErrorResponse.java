package com.microservice.entertainment.models.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Long timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
