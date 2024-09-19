package com.microservice.entertainment.models.dto;

import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private Long id;
    private String name;
    private String description;
    private String avatar;
    private String country;
    private ProfessionType profession;
}
