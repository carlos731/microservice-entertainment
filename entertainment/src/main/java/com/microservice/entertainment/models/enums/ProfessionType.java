package com.microservice.entertainment.models.enums;

import lombok.Getter;

@Getter
public enum ProfessionType {
    ACTOR(0, "Actor"),
    ACTRESS(1, "Actress"),
    DIRECTOR(1, "Director"),
    PRODUCER(2, "Producer"),
    SCREENWRITER(3, "Screenwritter"),
    COMPOSER(4, "Composer"),
    DEVELOPER(5, "Developer"),
    SINGER(6, "Singer");

    private final Integer code;
    private final String description;

    ProfessionType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    // Método para converter código em enum
    public static ProfessionType toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (ProfessionType x : ProfessionType.values()) {
            if (cod.equals(x.getCode())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid Function code: " + cod);
    }
}
