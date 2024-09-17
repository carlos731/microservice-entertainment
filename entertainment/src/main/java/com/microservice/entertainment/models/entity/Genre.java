package com.microservice.entertainment.models.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;
    @Column(name = "name")
    private String name;

    private static String generateHexId() {
        Random random = new Random();
        int randomNumber = random.nextInt(0x10000000);
        return String.format("%08x", randomNumber);
    }
}
