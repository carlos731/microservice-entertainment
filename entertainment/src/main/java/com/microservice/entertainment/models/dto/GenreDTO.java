package com.microservice.entertainment.models.dto;

import com.microservice.entertainment.models.entity.Entertainment;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Long id;
    private String name;
}
