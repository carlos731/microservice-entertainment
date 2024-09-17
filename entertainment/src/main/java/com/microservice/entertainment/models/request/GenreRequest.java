package com.microservice.entertainment.models.request;

import com.microservice.entertainment.models.entity.Entertainment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequest {
    @NotNull(message = "The name field is required!")
    private String name;
}
