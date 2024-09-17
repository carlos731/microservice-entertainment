package com.microservice.entertainment.models.entity;

import com.microservice.entertainment.models.enums.ProfessionType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_cast")
public class Cast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cast_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "entertainment_id", referencedColumnName = "entertainment_id")
    private Entertainment entertainmentId;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person personId;
    private String character;
    private ProfessionType profession;
}
