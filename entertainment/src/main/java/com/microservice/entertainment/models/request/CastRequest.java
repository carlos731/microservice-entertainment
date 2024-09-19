package com.microservice.entertainment.models.request;

import com.microservice.entertainment.models.enums.ProfessionType;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CastRequest {
    private Long id;
    private Long entertainmentId;
    private Long personId;
    private String character;
    private ProfessionType profession;
}
