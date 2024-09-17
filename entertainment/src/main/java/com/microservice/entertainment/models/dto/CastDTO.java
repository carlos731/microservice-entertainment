package com.microservice.entertainment.models.dto;

import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.entity.Person;
import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CastDTO {
    private Long id;
    private Entertainment entertainmentId;
    private Person personId;
    private String character;
    private ProfessionType profession;
}
