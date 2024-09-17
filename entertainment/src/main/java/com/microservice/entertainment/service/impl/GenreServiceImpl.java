package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.exception.CustomException;
import com.microservice.entertainment.models.dto.GenreDTO;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.mapper.GenreMapper;
import com.microservice.entertainment.repository.GenreRepository;
import com.microservice.entertainment.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Override
    public GenreDTO create(GenreDTO genreDTO) {
        Genre genre = genreMapper.mapDtoToEntity(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.mapEntityToDto(genre);
    }

    @Override
    public GenreDTO update(Long id, GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND,
                "Genre not found with id: " + id,
                HttpStatus.NOT_FOUND
        ));
        genre.setName(genreDTO.getName());
        genre = genreRepository.save(genre);
        return genreMapper.mapEntityToDto(genre);
    }

    @Override
    public List<GenreDTO> findAll() {
        List<Genre> genres = genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));;
        return genres.stream().map(genreMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<GenreDTO> findById(Long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isEmpty()) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Genre not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }
        return genre.map(genreMapper::mapEntityToDto);
    }

    @Override
    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new CustomException(
                    ErrorType.NOT_FOUND,
                    "Genre not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }
        genreRepository.deleteById(id);
    }
}
