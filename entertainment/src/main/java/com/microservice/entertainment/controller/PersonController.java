package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.PersonDTO;
import com.microservice.entertainment.models.mapper.PersonMapper;
import com.microservice.entertainment.models.request.PersonRequest;
import com.microservice.entertainment.models.response.ErrorResponse;
import com.microservice.entertainment.models.response.PersonResponse;
import com.microservice.entertainment.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(
        name = "CRUD REST APIs for Person",
        description = "CRUD REST APIs in Person to CREATE, UPDATE, FETCH AND DELETE Persons details"
)
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService service;
    @Autowired
    private PersonMapper mapper;

    @Operation(
            summary = "Metodo para criação de pessoas.",
            description = "Requisição para criar pessoas no banco de dados e retornar a pessoa salva.",
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
    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest request) {
        PersonDTO dto = mapper.mapRequestToDto(request);
        dto = service.create(dto);
        PersonResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Metodo para atualização da pessoa.",
            description = "Requisição para atualizar pessoas no banco de dados e retornar a pessoa atualizada.",
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
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> udpate(@Valid @RequestBody PersonRequest request, @PathVariable Long id) {
        PersonDTO dto = mapper.mapRequestToDto(request);
        dto = service.update(id, dto);
        PersonResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca todos as pessoas.",
            description = "Busca todos as pessoas do banco de dados."
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
    @GetMapping
    public ResponseEntity<List<PersonResponse>> findAll() {
        List<PersonDTO> results = service.findAll();
        List<PersonResponse> response = results
                .stream()
                .map(mapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca a pessoa por id.",
            description = "Busca a pessoa por id."
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
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        Optional<PersonDTO> dto = service.findById(id);
        PersonResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove a pessoa por id.",
            description = "Remove a pessoa por id.",
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
    @PreAuthorize("hasAuthority('delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
