package com.microservice.entertainment.models.response;

import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.entity.Genre;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentResponse {
    private Long id;
    private String title;
    private String description;
    private String country;
    private String trailer;
    private String posterImg;
    private String coverImg;
    private Float averageScore;
    private Boolean isVisibility;
    private LocalDate launch;
    private LocalDateTime created;
    private LocalDateTime updated;
    private EntertainmentType entertainmentTypeId;
    private List<Genre> genres;
}
