package com.microservice.entertainment.models.response;


import com.microservice.entertainment.models.entity.Genre;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentMovieResponse {
    // Entertainment:
    private String title;
    private String description;
    private String country;
    private String posterImg;
    private String coverImg;
    private Float averageScore;
    private Boolean isVisibility;
    private LocalDate launch;
    private Long entertainmentTypeId;
    private List<Genre> genres;
    // Movie:
    private String trailer;
    private String video;
    private int duration;
}
