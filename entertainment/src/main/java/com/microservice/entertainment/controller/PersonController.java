package com.microservice.entertainment.controller;

import com.microservice.entertainment.exception.FieldMessage;
import com.microservice.entertainment.exception.ValidationError;
import com.microservice.entertainment.models.dto.PersonDTO;
import com.microservice.entertainment.models.enums.ProfessionType;
import com.microservice.entertainment.models.mapper.PersonMapper;
import com.microservice.entertainment.models.request.PersonRequest;
import com.microservice.entertainment.models.response.ErrorResponse;
import com.microservice.entertainment.models.response.PersonResponse;
import com.microservice.entertainment.service.PersonService;
import com.microservice.entertainment.service.UploadService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    private UploadService uploadService;
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
    @PostMapping("/add")
    public ResponseEntity<Object> add(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "avatar") MultipartFile avatar,
            @RequestParam(name = "country") String country,
            @RequestParam(name = "profession", required = false) Integer professionCode
    ) throws IOException {
        List<FieldMessage> validationErrors = new ArrayList<>();

        if (name == null || name.isEmpty()) {
            validationErrors.add(new FieldMessage("name", "The name field is required!"));
        }
        if (description == null || description.isEmpty()) {
            validationErrors.add(new FieldMessage("description", "The description field is required!"));
        }
        if (!avatar.isEmpty() && !isImage(avatar)) {
            validationErrors.add(new FieldMessage("avatar", "For the avatar it is mandatory to select an image!"));
        }
        if (country == null || country.isEmpty()) {
            validationErrors.add(new FieldMessage("country", "The country field is required!"));
        }
        ProfessionType professionType = null;
        if (professionCode != null) {
            try {
                professionType = ProfessionType.toEnum(professionCode);
            } catch (IllegalArgumentException e) {
                validationErrors.add(new FieldMessage("profession", "Invalid profession code provided."));
            }
        }

        if (!validationErrors.isEmpty()) {
            ValidationError errorsResponse = new ValidationError(
                    System.currentTimeMillis(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation error",
                    "Error in the validation of the fields",
                    "/person/add"
            );

            for (FieldMessage error : validationErrors) {
                errorsResponse.addError(error.getFieldName(), error.getMessage());
            }
            return ResponseEntity.badRequest().body(errorsResponse);
        }

        String avatarUrl = null;
        try {
            if (!avatar.isEmpty() && isImage(avatar)) {
                avatarUrl = uploadService.upload(avatar);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Upload service is currently unavailable. Please try again later.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        PersonRequest request = new PersonRequest();
        request.setName(name);
        request.setDescription(description);
        request.setAvatar(avatarUrl);
        request.setCountry(country);
        request.setProfession(professionType);

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
    @PreAuthorize("hasAuthority('delete:entertainment')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("image/"));
    }
}
