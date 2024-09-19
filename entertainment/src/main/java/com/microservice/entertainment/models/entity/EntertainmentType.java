package com.microservice.entertainment.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_entertainment_type")
public class EntertainmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entertainment_type_id")
    private Long id;
    @Column(name = "name")
    private String name;
}
