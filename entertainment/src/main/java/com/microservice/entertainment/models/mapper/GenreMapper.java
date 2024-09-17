package com.microservice.entertainment.models.mapper;

import com.microservice.entertainment.models.dto.GenreDTO;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.request.GenreRequest;
import com.microservice.entertainment.models.response.GenreResponse;
import org.springframework.stereotype.Service;

@Service
public class GenreMapper {
    public GenreDTO mapEntityToDto(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public Genre mapDtoToEntity(GenreDTO genreDTO) {
        Genre entity = new Genre();
        entity.setId(genreDTO.getId());
        entity.setName(genreDTO.getName());
        return entity;
    }

    public GenreDTO mapRequestToDto(GenreRequest request) {
        GenreDTO dto = new GenreDTO();
        dto.setName(request.getName());
        return dto;
    }

    public GenreResponse mapDtoToResponse(GenreDTO dto) {
        GenreResponse response = new GenreResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        return response;
    }
}
