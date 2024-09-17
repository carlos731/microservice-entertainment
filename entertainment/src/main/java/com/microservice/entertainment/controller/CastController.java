package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.CastDTO;
import com.microservice.entertainment.models.request.CastRequest;
import com.microservice.entertainment.models.response.CastPersonResponse;
import com.microservice.entertainment.models.response.CastResponse;
import com.microservice.entertainment.service.CastService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cast")
public class CastController {
    @Autowired
    private CastService service;
    ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<CastResponse> create(@Valid @RequestBody CastRequest request) {
        CastDTO dto = modelMapper.map(request, CastDTO.class);
        dto = service.create(dto);
        return new ResponseEntity<>(modelMapper.map(dto, CastResponse.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CastResponse> update(@Valid @RequestBody CastRequest request, @PathVariable Long id) {
        CastDTO dto = modelMapper.map(request, CastDTO.class);
        dto = service.update(id, dto);
        return new ResponseEntity<>(modelMapper.map(dto, CastResponse.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CastResponse>> findAll(){
        List<CastDTO> categoriesDTO = service.findAll();
        ModelMapper mapper = new ModelMapper();
        List<CastResponse> categoriesResponse = categoriesDTO.stream().map(
                cast -> mapper.map(cast, CastResponse.class)).collect(Collectors.toList());
        return new ResponseEntity<>(categoriesResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CastResponse>> findById(@PathVariable Long id){
        Optional<CastDTO> castDTO = service.findById(id);
        CastResponse castResponse = new ModelMapper().map(castDTO.get(), CastResponse.class);
        return new ResponseEntity<>(Optional.of(castResponse), HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<CastPersonResponse>> findCastByEntertainmentId(@RequestParam(value = "entertainmentId") Long id) {
        List<CastPersonResponse> results = service.findCastByEntertainmentId(id);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
