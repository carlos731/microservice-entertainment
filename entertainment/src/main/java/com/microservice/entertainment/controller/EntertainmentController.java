package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.pagination.EntertainmentPagination;
import com.microservice.entertainment.models.request.EntertainmentFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.microservice.entertainment.models.mapper.EntertainmentMapper;
import com.microservice.entertainment.models.request.EntertainmentRequest;
import com.microservice.entertainment.models.response.EntertainmentResponse;
import com.microservice.entertainment.service.EntertainmentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/entertainment")
public class EntertainmentController {
    @Autowired
    private EntertainmentService service;
    @Autowired
    private EntertainmentMapper mapper;
    @Autowired
    private PagedResourcesAssembler<EntertainmentDTO> pagedResourcesAssembler;

    ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/add")
    public ResponseEntity<EntertainmentResponse> create(@Valid @RequestBody EntertainmentRequest request) {
        EntertainmentDTO dto = modelMapper.map(request, EntertainmentDTO.class);
        dto = service.create(dto);
        return new ResponseEntity<>(modelMapper.map(dto, EntertainmentResponse.class), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntertainmentResponse> update(@Valid @RequestBody EntertainmentRequest request, @PathVariable Long id) {
        EntertainmentDTO dto = modelMapper.map(request, EntertainmentDTO.class);
        dto = service.update(id, dto);
        return new ResponseEntity<>(modelMapper.map(dto, EntertainmentResponse.class), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<EntertainmentPagination> findByFilters(@RequestBody EntertainmentFilterRequest request) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(request.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortBy = Sort.by(sortDirection, request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Page<EntertainmentDTO> results = service.findByFilters(pageable, request);

        List<EntertainmentResponse> responses = results.stream()
                .map(dto -> mapper.mapDtoToResponse(dto))
                .collect(Collectors.toList());

        EntertainmentPagination responseWrapper = new EntertainmentPagination(
                responses,
                new PagedModel.PageMetadata(request.getSize(), request.getPage(), results.getTotalElements())
        );

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<EntertainmentPagination> search(@Valid @RequestBody EntertainmentFilterRequest request) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(request.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortBy = Sort.by(sortDirection, request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Page<EntertainmentDTO> results = service.search(pageable, request);

        List<EntertainmentResponse> responses = results.stream()
                .map(dto -> mapper.mapDtoToResponse(dto))
                .collect(Collectors.toList());

        EntertainmentPagination responseWrapper = new EntertainmentPagination(
                responses,
                new PagedModel.PageMetadata(request.getSize(), request.getPage(), results.getTotalElements())
        );

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentResponse> findById(@PathVariable Long id) {
        Optional<EntertainmentDTO> dto = service.findById(id);
        EntertainmentResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
    @GetMapping
    public ResponseEntity<EntertainmentPagination> findByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "launch") String sort,
            @RequestParam(required = false) List<Long> entertainmentTypeIds,
            @RequestParam(required = false) List<Long> genreIds
    ) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortBy = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<EntertainmentDTO> results = service.findByFilters(pageable, entertainmentTypeIds, genreIds);

        List<EntertainmentResponse> responses = results.stream()
                .map(dto -> mapper.mapDtoToResponse(dto))
                .collect(Collectors.toList());

        Map<String, String> links = createNavigationLinks(page, size, direction, sort, results, entertainmentTypeIds, genreIds);

        EntertainmentPagination responseWrapper = new EntertainmentPagination(
                responses,
                new PagedModel.PageMetadata(size, page, results.getTotalElements()),
                links
        );

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }



    private Map<String, String> createNavigationLinks(int page, int size, String direction, String sort, Page<EntertainmentDTO> results,
                                                      List<Long> entertainmentTypeIds, List<Long> genreIds) {
        Map<String, String> links = new HashMap<>();

        // Link para a página atual
        links.put("self", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntertainmentController.class)
                .findByFilters(page, size, direction, sort, entertainmentTypeIds, genreIds)).toUri().toString());

        // Link para a próxima página
        if (results.hasNext()) {
            links.put("next", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntertainmentController.class)
                    .findByFilters(page + 1, size, direction, sort, entertainmentTypeIds, genreIds)).toUri().toString());
        }

        // Link para a página anterior
        if (results.hasPrevious()) {
            links.put("previous", WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntertainmentController.class)
                    .findByFilters(page - 1, size, direction, sort, entertainmentTypeIds, genreIds)).toUri().toString());
        }

        return links;
    }
     */
}
