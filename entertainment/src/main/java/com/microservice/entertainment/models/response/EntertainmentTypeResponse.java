package com.microservice.entertainment.models.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentTypeResponse {
    private Long id;
    private String name;
}
