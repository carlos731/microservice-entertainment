package com.microservice.entertainment.models.response;

import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CastPersonResponse {
    private Long castId;
    private Long personId;
    private String character;
    private String name;
    private String country;
    private String avatar;
    private ProfessionType profession;
}
