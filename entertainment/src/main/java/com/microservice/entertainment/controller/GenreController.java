package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.GenreDTO;
import com.microservice.entertainment.models.mapper.GenreMapper;
import com.microservice.entertainment.models.request.GenreRequest;
import com.microservice.entertainment.models.response.GenreResponse;
import com.microservice.entertainment.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreMapper genreMapper;

    @PostMapping
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request) {
        GenreDTO genreDTO = genreMapper.mapRequestToDto(request);
        genreDTO = genreService.create(genreDTO);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> udpate(@Valid @RequestBody GenreRequest request, @PathVariable Long id) {
        GenreDTO genreDTO = genreMapper.mapRequestToDto(request);
        genreDTO = genreService.update(id, genreDTO);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll() {
        List<GenreDTO> genresDTO = genreService.findAll();
        List<GenreResponse> responses = genresDTO
                .stream()
                .map(genreMapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> findById(@PathVariable Long id) {
        Optional<GenreDTO> genreDTO = genreService.findById(id);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
