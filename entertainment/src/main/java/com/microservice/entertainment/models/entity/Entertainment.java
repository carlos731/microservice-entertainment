package com.microservice.entertainment.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_entertainment")
public class Entertainment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entertainment_id")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title should not exceed 255 characters")
    @Column(name = "title")
    private String title;

    @Size(max = 2000, message = "Description should not exceed 2000 characters")
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "country")
    private String country;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "poster_img")
    private String posterImg;

    @Column(name = "cover_img")
    private String coverImg;

    @Min(value = 0, message = "Average score must be positive")
    @Max(value = 5, message = "Average score must not exceed 10")
    @Column(name = "average_score")
    private Float averageScore;

    @NotNull(message = "Visibility status is required")
    @Column(name = "is_visibility")
    private Boolean isVisibility;

    @Column(name = "launch")
    private LocalDate launch;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "entertainment_type_id", referencedColumnName = "entertainment_type_id")
    private EntertainmentType entertainmentTypeId;

    @ManyToMany
    @JoinTable(
            name = "tb_genre_entertainment",
            joinColumns = {@JoinColumn(name = "entertainment_id", referencedColumnName = "entertainment_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id", referencedColumnName = "genre_id")}
    )
    private List<Genre> genres;

    @OneToOne(mappedBy = "entertainment", cascade = CascadeType.REMOVE)
    private Movie movie;
}
