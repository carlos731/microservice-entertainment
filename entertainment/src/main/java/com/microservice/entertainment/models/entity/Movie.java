package com.microservice.entertainment.models.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "entertainment_id", referencedColumnName = "entertainment_id")
    private Entertainment entertainment;
    private String trailer;
    private String video;
    private int duration;
}
