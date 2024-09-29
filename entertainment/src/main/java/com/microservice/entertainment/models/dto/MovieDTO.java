package com.microservice.entertainment.models.dto;


import com.microservice.entertainment.models.entity.Entertainment;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private Entertainment entertainment;
    private String trailer;
    private String video;
    private int duration;
}
