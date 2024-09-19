package com.microservice.entertainment.models.response;

import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.entity.Person;
import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CastResponse {
    private Long id;
    private Entertainment entertainmentId;
    private Person personId;
    private String character;
    private ProfessionType profession;
}

