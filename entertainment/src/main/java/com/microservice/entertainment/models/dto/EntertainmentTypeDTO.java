package com.microservice.entertainment.models.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentTypeDTO {
    private Long id;
    private String name;
}
