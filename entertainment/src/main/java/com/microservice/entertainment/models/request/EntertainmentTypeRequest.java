package com.microservice.entertainment.models.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentTypeRequest {
    @NotNull(message = "The name field is required!")
    private String name;
}
