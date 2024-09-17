package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.GenreDTO;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    public GenreDTO create(GenreDTO genreDTO);
    public GenreDTO update(Long id, GenreDTO categoryDTO);
    public List<GenreDTO> findAll();
    public Optional<GenreDTO> findById(Long id);
    public void delete(Long id);
}
