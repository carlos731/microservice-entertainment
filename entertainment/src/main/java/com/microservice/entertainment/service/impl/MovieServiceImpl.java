package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.dto.MovieDTO;
import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.entity.Movie;
import com.microservice.entertainment.repository.EntertainmentRepository;
import com.microservice.entertainment.repository.MovieRepository;
import com.microservice.entertainment.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private EntertainmentRepository entertainmentRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public MovieDTO create(EntertainmentDTO entertainmentDTO, MovieDTO movieDTO) {
        // save entertainment
        Entertainment entertainment = modelMapper.map(entertainmentDTO, Entertainment.class);
        entertainment.setCreated(LocalDateTime.now());
        entertainment = entertainmentRepository.save(entertainment);

        // save movie
        Movie movie = modelMapper.map(movieDTO, Movie.class);
        movie.setEntertainment(entertainment);
        movie = movieRepository.save(movie);
        
        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public MovieDTO update(Long entertainmentId, MovieDTO movieDTO) {
        
        return null;
    }

    @Override
    public List<MovieDTO> findAll() {
        List<Movie> movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return movies.stream().map(movie -> modelMapper.map(movie, MovieDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<MovieDTO> findByEntertainmentId(Long entertainmentId) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }
}
