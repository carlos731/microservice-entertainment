package com.microservice.entertainment.models.response;

import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private Long id;
    private String name;
    private String description;
    private String avatar;
    private String country;
    private ProfessionType profession;
}
