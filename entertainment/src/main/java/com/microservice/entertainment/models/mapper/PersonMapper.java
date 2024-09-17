package com.microservice.entertainment.models.mapper;

import com.microservice.entertainment.models.dto.PersonDTO;
import com.microservice.entertainment.models.entity.Person;
import com.microservice.entertainment.models.request.PersonRequest;
import com.microservice.entertainment.models.response.PersonResponse;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {
    public PersonDTO mapEntityToDto(Person entity) {
        PersonDTO dto = new PersonDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setAvatar(entity.getAvatar());
        dto.setCountry(entity.getCountry());
        dto.setProfession(entity.getProfession());
        return dto;
    }

    public Person mapDtoToEntity(PersonDTO dto) {
        Person entity = new Person();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAvatar(dto.getAvatar());
        entity.setCountry(dto.getCountry());
        entity.setProfession(dto.getProfession());
        return entity;
    }

    public PersonDTO mapRequestToDto(PersonRequest request) {
        PersonDTO dto = new PersonDTO();
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setAvatar(request.getAvatar());
        dto.setCountry(request.getCountry());
        dto.setProfession(request.getProfession());
        return dto;
    }

    public PersonResponse mapDtoToResponse(PersonDTO dto) {
        PersonResponse response = new PersonResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        response.setDescription(dto.getDescription());
        response.setAvatar(dto.getAvatar());
        response.setCountry(dto.getCountry());
        response.setProfession(dto.getProfession());
        return response;
    }
}
