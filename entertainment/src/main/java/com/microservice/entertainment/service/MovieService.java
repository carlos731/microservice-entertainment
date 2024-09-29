package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.dto.MovieDTO;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    public MovieDTO create(EntertainmentDTO entertainmentDTO, MovieDTO movieDTO);
    public MovieDTO update(Long entertainmentId, MovieDTO movieDTO);
    public List<MovieDTO> findAll();
    public Optional<MovieDTO> findById(Long id);
    public Optional<MovieDTO> findByEntertainmentId(Long entertainmentId);
    public void delete(Long id);
}
