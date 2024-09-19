package com.microservice.entertainment.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.entity.Genre;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentRequest {
    private Long id;
    @NotNull(message = "The title field is required!")
    private String title;
    @NotNull(message = "The description field is required!")
    private String description;
    @NotNull(message = "The country field is required!")
    private String country;
    @NotNull(message = "The trailer field is required!")
    private String trailer;
    @NotNull(message = "The posterImg field is required!")
    private String posterImg;
    @NotNull(message = "The coverImg field is required!")
    private String coverImg;
    @NotNull(message = "The averageScore field is required!")
    private Float averageScore;
    @NotNull(message = "The isVisibility field is required!")
    private Boolean isVisibility;
    @NotNull(message = "The launchAt field is required!")
    private LocalDate launch;
    private LocalDateTime created;
    private LocalDateTime updated;
    @NotNull(message = "The entertainmentTypeId field is required!")
    private Long entertainmentTypeId;
    @NotNull(message = "The genres field is required!")
    private List<Genre> genres;
}
