package com.microservice.entertainment.models.request;

import com.microservice.entertainment.models.enums.ProfessionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
    @NotNull(message = "The name field is required!")
    private String name;
    @NotNull(message = "The description field is required!")
    private String description;
    @NotNull(message = "The avatar field is required!")
    private String avatar;
    @NotNull(message = "The country field is required!")
    private String country;
    @NotNull(message = "The profession field is required!")
    private ProfessionType profession;
}
