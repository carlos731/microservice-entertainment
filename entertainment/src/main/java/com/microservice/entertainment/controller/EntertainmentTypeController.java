package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.mapper.EntertainmentTypeMapper;
import com.microservice.entertainment.models.request.EntertainmentTypeRequest;
import com.microservice.entertainment.models.request.EntertainmentTypeRequest;
import com.microservice.entertainment.models.response.EntertainmentTypeResponse;
import com.microservice.entertainment.models.response.EntertainmentTypeResponse;
import com.microservice.entertainment.service.EntertainmentTypeService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entertainment-type")
public class EntertainmentTypeController {
    @Autowired
    private EntertainmentTypeService service;
    @Autowired
    private EntertainmentTypeMapper mapper;

    @PostMapping
    public ResponseEntity<EntertainmentTypeResponse> create(@Valid @RequestBody EntertainmentTypeRequest request) {
        EntertainmentTypeDTO dto = mapper.mapRequestToDto(request);
        dto = service.create(dto);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntertainmentTypeResponse> udpate(@Valid @RequestBody EntertainmentTypeRequest request, @PathVariable Long id) {
        EntertainmentTypeDTO dto = mapper.mapRequestToDto(request);
        dto = service.update(id, dto);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EntertainmentTypeResponse>> findAll() {
        List<EntertainmentTypeDTO> results = service.findAll();
        List<EntertainmentTypeResponse> response = results
                .stream()
                .map(mapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentTypeResponse> findById(@PathVariable Long id) {
        Optional<EntertainmentTypeDTO> dto = service.findById(id);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
