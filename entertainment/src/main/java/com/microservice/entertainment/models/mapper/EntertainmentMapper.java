package com.microservice.entertainment.models.mapper;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.request.EntertainmentRequest;
import com.microservice.entertainment.models.response.EntertainmentResponse;
import org.springframework.stereotype.Service;

@Service
public class EntertainmentMapper {
    public EntertainmentDTO mapEntityToDto(Entertainment entity) {
        EntertainmentDTO dto = new EntertainmentDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCountry(entity.getCountry());
        dto.setTrailer(entity.getTrailer());
        dto.setPosterImg(entity.getPosterImg());
        dto.setCoverImg(entity.getCoverImg());
        dto.setAverageScore(entity.getAverageScore());
        dto.setIsVisibility(entity.getIsVisibility());
        dto.setLaunch(entity.getLaunch());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        dto.setEntertainmentTypeId(entity.getEntertainmentTypeId());
        dto.setGenres(entity.getGenres());
        return dto;
    }

    public Entertainment mapDtoToEntity(EntertainmentDTO dto) {
        Entertainment entity = new Entertainment();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCountry(dto.getCountry());
        entity.setTrailer(dto.getTrailer());
        entity.setPosterImg(dto.getPosterImg());
        entity.setCoverImg(dto.getCoverImg());
        entity.setAverageScore(dto.getAverageScore());
        entity.setIsVisibility(dto.getIsVisibility());
        entity.setLaunch(dto.getLaunch());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        entity.setEntertainmentTypeId(dto.getEntertainmentTypeId());
        entity.setGenres(dto.getGenres());
        return entity;
    }

    public EntertainmentDTO mapRequestToDto(EntertainmentRequest request) {
        EntertainmentDTO dto = new EntertainmentDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setCountry(request.getCountry());
        dto.setTrailer(request.getTrailer());
        dto.setPosterImg(request.getPosterImg());
        dto.setCoverImg(request.getCoverImg());
        dto.setAverageScore(request.getAverageScore());
        dto.setIsVisibility(request.getIsVisibility());
        dto.setLaunch(request.getLaunch());
        dto.setCreated(request.getCreated());
        dto.setUpdated(request.getUpdated());

        EntertainmentType entertainmentType = new EntertainmentType();
        entertainmentType.setId(request.getEntertainmentTypeId());
        dto.setEntertainmentTypeId(entertainmentType);

        dto.setGenres(request.getGenres());
        return dto;
    }

    public EntertainmentResponse mapDtoToResponse(EntertainmentDTO dto) {
        EntertainmentResponse response = new EntertainmentResponse();
        response.setId(dto.getId());
        response.setTitle(dto.getTitle());
        response.setDescription(dto.getDescription());
        response.setCountry(dto.getCountry());
        response.setTrailer(dto.getTrailer());
        response.setPosterImg(dto.getPosterImg());
        response.setCoverImg(dto.getCoverImg());
        response.setAverageScore(dto.getAverageScore());
        response.setIsVisibility(dto.getIsVisibility());
        response.setLaunch(dto.getLaunch());
        response.setCreated(dto.getCreated());
        response.setUpdated(dto.getUpdated());
        response.setEntertainmentTypeId(dto.getEntertainmentTypeId());
        response.setGenres(dto.getGenres());
        return response;
    }
}
