package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.GenreDTO;
import com.microservice.entertainment.models.mapper.GenreMapper;
import com.microservice.entertainment.models.request.GenreRequest;
import com.microservice.entertainment.models.response.ErrorResponse;
import com.microservice.entertainment.models.response.GenreResponse;
import com.microservice.entertainment.service.GenreService;
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
        name = "CRUD REST APIs for Genre",
        description = "CRUD REST APIs in Genre to CREATE, UPDATE, FETCH AND DELETE Genres details"
)
@RestController
@RequestMapping("/genre")
public class GenreController {
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreMapper genreMapper;

    @Operation(
            summary = "Metodo para criação de gêneros.",
            description = "Requisição para criar gêneros no banco de dados e retornar o gênero salvo.",
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
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request) {
        GenreDTO genreDTO = genreMapper.mapRequestToDto(request);
        genreDTO = genreService.create(genreDTO);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Metodo para atualização do gênero.",
            description = "Requisição para atualizar gêneros no banco de dados e retornar o gênero atualizado.",
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
    public ResponseEntity<GenreResponse> udpate(@Valid @RequestBody GenreRequest request, @PathVariable Long id) {
        GenreDTO genreDTO = genreMapper.mapRequestToDto(request);
        genreDTO = genreService.update(id, genreDTO);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca todos os gêneros.",
            description = "Busca todos os gêneros do banco de dados."
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
    public ResponseEntity<List<GenreResponse>> findAll() {
        List<GenreDTO> genresDTO = genreService.findAll();
        List<GenreResponse> responses = genresDTO
                .stream()
                .map(genreMapper::mapDtoToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(
            summary = "Busca gênero por id.",
            description = "Busca gênero por id."
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
    public ResponseEntity<GenreResponse> findById(@PathVariable Long id) {
        Optional<GenreDTO> genreDTO = genreService.findById(id);
        GenreResponse response = genreMapper.mapDtoToResponse(genreDTO.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove o gênero por id.",
            description = "Remove o gênero por id.",
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
        genreService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
