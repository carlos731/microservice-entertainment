package com.microservice.entertainment.models.response;

import com.microservice.entertainment.models.entity.Entertainment;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponse {
    private Long id;
    private String name;
}
