package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.mapper.EntertainmentTypeMapper;
import com.microservice.entertainment.models.request.EntertainmentTypeRequest;
import com.microservice.entertainment.models.response.EntertainmentTypeResponse;
import com.microservice.entertainment.models.response.ErrorResponse;
import com.microservice.entertainment.service.EntertainmentTypeService;
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
        name = "CRUD REST APIs for EntertainmentType",
        description = "CRUD REST APIs in EntertainmentType to CREATE, UPDATE, FETCH AND DELETE EntertainmentTypes details"
)
@RestController
@RequestMapping("/entertainment-type")
public class EntertainmentTypeController {
    @Autowired
    private EntertainmentTypeService service;
    @Autowired
    private EntertainmentTypeMapper mapper;

    @Operation(
            summary = "Metodo para criação de tipo de entretenimento.",
            description = "Requisição para criar tipos de entretenimentos no banco de dados e retornar o tipo de entretenimento salvo.",
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
    public ResponseEntity<EntertainmentTypeResponse> create(@Valid @RequestBody EntertainmentTypeRequest request) {
        EntertainmentTypeDTO dto = mapper.mapRequestToDto(request);
        dto = service.create(dto);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Metodo para atualização de tipo de entretenimento.",
            description = "Requisição para atualizar tipos de entretenimentos no banco de dados e retornar o tipo de entretenimento atualizado.",
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
    public ResponseEntity<EntertainmentTypeResponse> udpate(@Valid @RequestBody EntertainmentTypeRequest request, @PathVariable Long id) {
        EntertainmentTypeDTO dto = mapper.mapRequestToDto(request);
        dto = service.update(id, dto);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca todos os tipos de entretenimentos.",
            description = "Busca todos os tipos de entretenimentos do banco de dados."
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
    public ResponseEntity<List<EntertainmentTypeResponse>> findAll() {
        List<EntertainmentTypeDTO> results = service.findAll();
        List<EntertainmentTypeResponse> response = results
                .stream()
                .map(mapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca tipo de entretenimento por id.",
            description = "Busca tipo de entretenimento por id."
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
    public ResponseEntity<EntertainmentTypeResponse> findById(@PathVariable Long id) {
        Optional<EntertainmentTypeDTO> dto = service.findById(id);
        EntertainmentTypeResponse response = mapper.mapDtoToResponse(dto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove o tipo do entretenimento por id.",
            description = "Remove o tipo do entretenimento por id.",
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
