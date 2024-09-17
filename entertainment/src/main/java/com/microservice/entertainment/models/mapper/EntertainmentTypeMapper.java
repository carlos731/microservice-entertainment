package com.microservice.entertainment.models.mapper;

import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.request.EntertainmentTypeRequest;
import com.microservice.entertainment.models.response.EntertainmentTypeResponse;
import org.springframework.stereotype.Service;

@Service
public class EntertainmentTypeMapper {
    public EntertainmentTypeDTO mapEntityToDto(EntertainmentType entity) {
        EntertainmentTypeDTO dto = new EntertainmentTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public EntertainmentType mapDtoToEntity(EntertainmentTypeDTO dto) {
        EntertainmentType entity = new EntertainmentType();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    public EntertainmentTypeDTO mapRequestToDto(EntertainmentTypeRequest request) {
        EntertainmentTypeDTO dto = new EntertainmentTypeDTO();
        dto.setName(request.getName());
        return dto;
    }

    public EntertainmentTypeResponse mapDtoToResponse(EntertainmentTypeDTO dto) {
        EntertainmentTypeResponse response = new EntertainmentTypeResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        return response;
    }
}
