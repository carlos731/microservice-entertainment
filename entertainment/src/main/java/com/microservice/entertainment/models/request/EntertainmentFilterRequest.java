package com.microservice.entertainment.models.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentFilterRequest {
    private List<Long> entertainmentTypeIds = new ArrayList<>(); // []
    private List<Long> genreIds = new ArrayList<>(); // []
    private String direction = "desc";
    private String sort = "launch";
    private List<Integer> years = new ArrayList<>();
    @NotNull(message = "Title is required!")
    private String title = "";
    private int page = 0;
    private int size = 10;
    private Boolean isVisibility = null;
}
