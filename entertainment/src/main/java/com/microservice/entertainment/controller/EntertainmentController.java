package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.pagination.EntertainmentPagination;
import com.microservice.entertainment.models.request.EntertainmentFilterRequest;
import com.microservice.entertainment.models.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(
        name = "CRUD REST APIs for Entertainment",
        description = "CRUD REST APIs in Entertainment to CREATE, UPDATE, FETCH AND DELETE entertainments details"
)
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

    @Operation(
            summary = "Metodo para criação de entretenimentos.",
            description = "Requisição para criar entretenimentos no banco de dados e retornar o entertenimento salvo.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/add")
    public ResponseEntity<EntertainmentResponse> create(@Valid @RequestBody EntertainmentRequest request) {
        EntertainmentDTO dto = modelMapper.map(request, EntertainmentDTO.class);
        dto = service.create(dto);
        return new ResponseEntity<>(modelMapper.map(dto, EntertainmentResponse.class), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Metodo para atualização de entretenimentos.",
            description = "Requisição para atualizar entretenimentos no banco de dados e retornar o entertenimento salvo.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NotFound"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<EntertainmentResponse> update(@Valid @RequestBody EntertainmentRequest request, @PathVariable Long id) {
        EntertainmentDTO dto = modelMapper.map(request, EntertainmentDTO.class);
        dto = service.update(id, dto);
        return new ResponseEntity<>(modelMapper.map(dto, EntertainmentResponse.class), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca paginada por filtros de entretenimentos.",
            description = "Busca paginada por filtros de entretenimentos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NotFound"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
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

    @Operation(
            summary = "Pesquisa por entretenimentos.",
            description = "Pesquisa por entretenimentos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NotFound"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
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

    @Operation(
            summary = "Busca Entretenimento por id.",
            description = "Busca Entretenimento por id."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NotFound"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentResponse> findById(@PathVariable Long id) {
        Optional<EntertainmentDTO> dto = service.findById(id);
        EntertainmentResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove o Entretenimento por id.",
            description = "Remove o Entretenimento por id.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NotFound"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "HTTP Status Unauthorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "HTTP Status Forbidden",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('delete:entertainment')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
