package com.microservice.entertainment.models.entity;

import com.microservice.entertainment.models.enums.ProfessionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    private String name;
    @Size(max = 2000, message = "Description should not exceed 2000 characters")
    @Column(name = "description", columnDefinition = "text")
    private String description;
    private String avatar;
    private String country;
    private ProfessionType profession;
}
