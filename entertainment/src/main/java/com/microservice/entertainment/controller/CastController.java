package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.CastDTO;
import com.microservice.entertainment.models.request.CastRequest;
import com.microservice.entertainment.models.response.CastPersonResponse;
import com.microservice.entertainment.models.response.CastResponse;
import com.microservice.entertainment.models.response.ErrorResponse;
import com.microservice.entertainment.service.CastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(
        name = "CRUD REST APIs for Cast",
        description = "CRUD REST APIs in Cast to CREATE, UPDATE, FETCH AND DELETE Cast details"
)
@RestController
@RequestMapping("/cast")
public class CastController {
    @Autowired
    private CastService service;
    ModelMapper modelMapper = new ModelMapper();

    @Operation(
            summary = "Metodo para criação de elenco.",
            description = "Requisição para criar elencos no banco de dados e retornar o elenco salvo.",
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
    public ResponseEntity<CastResponse> create(@Valid @RequestBody CastRequest request) {
        CastDTO dto = modelMapper.map(request, CastDTO.class);
        dto = service.create(dto);
        return new ResponseEntity<>(modelMapper.map(dto, CastResponse.class), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Metodo para atualização do elenco.",
            description = "Requisição para atualizar elencos no banco de dados e retornar o elenco atualizado.",
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
    public ResponseEntity<CastResponse> update(@Valid @RequestBody CastRequest request, @PathVariable Long id) {
        CastDTO dto = modelMapper.map(request, CastDTO.class);
        dto = service.update(id, dto);
        return new ResponseEntity<>(modelMapper.map(dto, CastResponse.class), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca todos os elencos.",
            description = "Busca todos os elencos do banco de dados."
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
    public ResponseEntity<List<CastResponse>> findAll(){
        List<CastDTO> categoriesDTO = service.findAll();
        ModelMapper mapper = new ModelMapper();
        List<CastResponse> categoriesResponse = categoriesDTO.stream().map(
                cast -> mapper.map(cast, CastResponse.class)).collect(Collectors.toList());
        return new ResponseEntity<>(categoriesResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca elenco por id.",
            description = "Busca elenco por id."
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
    public ResponseEntity<Optional<CastResponse>> findById(@PathVariable Long id){
        Optional<CastDTO> castDTO = service.findById(id);
        CastResponse castResponse = new ModelMapper().map(castDTO.get(), CastResponse.class);
        return new ResponseEntity<>(Optional.of(castResponse), HttpStatus.OK);
    }

    @Operation(
            summary = "Busca o elenco pelo id do entretenimento.",
            description = "Busca o elenco pelo id do entretenimento."
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
    @GetMapping("/find")
    public ResponseEntity<List<CastPersonResponse>> findCastByEntertainmentId(@RequestParam(value = "entertainmentId") Long id) {
        List<CastPersonResponse> results = service.findCastByEntertainmentId(id);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove o elenco por id.",
            description = "Remove o elenco por id.",
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
