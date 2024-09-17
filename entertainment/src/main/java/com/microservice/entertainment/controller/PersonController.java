package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.PersonDTO;
import com.microservice.entertainment.models.mapper.PersonMapper;
import com.microservice.entertainment.models.request.PersonRequest;
import com.microservice.entertainment.models.response.PersonResponse;
import com.microservice.entertainment.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService service;
    @Autowired
    private PersonMapper mapper;

    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest request) {
        PersonDTO dto = mapper.mapRequestToDto(request);
        dto = service.create(dto);
        PersonResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> udpate(@Valid @RequestBody PersonRequest request, @PathVariable Long id) {
        PersonDTO dto = mapper.mapRequestToDto(request);
        dto = service.update(id, dto);
        PersonResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> findAll() {
        List<PersonDTO> results = service.findAll();
        List<PersonResponse> response = results
                .stream()
                .map(mapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        Optional<PersonDTO> dto = service.findById(id);
        PersonResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
