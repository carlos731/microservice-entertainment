package com.microservice.entertainment.controller;

import com.microservice.entertainment.exception.FieldMessage;
import com.microservice.entertainment.exception.ValidationError;
import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.dto.MovieDTO;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.response.MovieResponse;
import com.microservice.entertainment.service.MovieService;
import com.microservice.entertainment.service.UploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UploadService uploadService;
    ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<Object> create(
            // referente ao entretenimento
            @RequestParam(name = "posterImg", required = false) MultipartFile posterImg,
            @RequestParam(name = "coverImg", required = false) MultipartFile coverImg,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "averageScore", required = false) Float averageScore,
            @RequestParam(name = "isVisibility", required = false) Boolean isVisibility,
            @RequestParam(name = "launch", required = false) LocalDate launch,
            @RequestParam(name = "entertainmentTypeId", required = false) EntertainmentType entertainmentTypeId,
            @RequestParam(name = "genres", required = false) List<Genre> genres,
            // referente ao movie
            @RequestParam(name = "trailer", required = false) MultipartFile trailer,
            @RequestParam(name = "video", required = false) MultipartFile video,
            @RequestParam(name = "duration", required = false) Integer duration
    ) throws IOException {
        // validar
        List<FieldMessage> validationErrors = validateFields(
                posterImg, coverImg, title, description, country, averageScore, isVisibility, launch, duration, entertainmentTypeId, genres, trailer, video
        );

        if (!validationErrors.isEmpty()) {
            ValidationError errorsResponse = new ValidationError(
                    System.currentTimeMillis(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation error",
                    "Error in the validation of the fields",
                    "/movie/add"
            );

            for (FieldMessage error : validationErrors) {
                errorsResponse.addError(error.getFieldName(), error.getMessage());
            }
            return ResponseEntity.badRequest().body(errorsResponse);
        }

        // Upload do arquivo
        String posterImgUrl = null;
        String coverImgUrl = null;
        String trailerUrl = null;
        String videoUrl = null;
        try {
            posterImgUrl = uploadService.upload(posterImg);
            coverImgUrl = uploadService.upload(coverImg);
            trailerUrl = uploadService.upload(trailer);
            videoUrl = uploadService.upload(video);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Upload service is currently unavailable. Please try again later.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        // instanciar
        // entertainment
        EntertainmentDTO entertainmentDTO = new EntertainmentDTO();
        entertainmentDTO.setPosterImg(posterImgUrl);
        entertainmentDTO.setCoverImg(coverImgUrl);
        entertainmentDTO.setTitle(title);
        entertainmentDTO.setTrailer(trailerUrl);
        entertainmentDTO.setDescription(description);
        entertainmentDTO.setIsVisibility(isVisibility);
        entertainmentDTO.setAverageScore(averageScore);
        entertainmentDTO.setLaunch(launch);
        entertainmentDTO.setCountry(country);
        entertainmentDTO.setEntertainmentTypeId(entertainmentTypeId);
        entertainmentDTO.setGenres(genres);

        // movie
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTrailer(trailerUrl);
        movieDTO.setVideo(videoUrl);
        movieDTO.setDuration(duration);

        // service
        movieDTO = movieService.create(entertainmentDTO, movieDTO);
        MovieResponse movieResponse = modelMapper.map(movieDTO, MovieResponse.class);

        // return
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    private List<FieldMessage> validateFields(
            MultipartFile posterImg,
            MultipartFile coverImg,
            String title,
            String description,
            String country,
            Float averageScore,
            Boolean isVisibility,
            LocalDate launch,
            Integer duration,
            EntertainmentType entertainmentTypeId,
            List<Genre> genres,
            MultipartFile trailer,
            MultipartFile video
    ) {
        List<FieldMessage> validationErrors = new ArrayList<>();

        // Validar campos obrigatórios
        if (isVisibility == null) {
            validationErrors.add(new FieldMessage("isVisibility", "The isVisibility field is required!"));
        }
        if (country == null || country.isEmpty()) {
            validationErrors.add(new FieldMessage("country", "The country field is required!"));
        }
        if (averageScore == null) {
            validationErrors.add(new FieldMessage("averageScore", "The averageScore field is required!"));
        }
        if (posterImg == null || posterImg.isEmpty()) {
            validationErrors.add(new FieldMessage("posterImg", "The posterImg field is required!"));
        } else if (!isImage(posterImg)) {
            validationErrors.add(new FieldMessage("posterImg", "The posterImg must be a valid image file!"));
        }
        if (coverImg == null || coverImg.isEmpty()) {
            validationErrors.add(new FieldMessage("coverImg", "The coverImg field is required!"));
        } else if (!isImage(coverImg)) {
            validationErrors.add(new FieldMessage("coverImg", "The coverImg must be a valid image file!"));
        }
        if (description == null || description.isEmpty()) {
            validationErrors.add(new FieldMessage("description", "The description field is required!"));
        }
        if (title == null || title.isEmpty()) {
            validationErrors.add(new FieldMessage("title", "The title field is required!"));
        }
        if (trailer == null || trailer.isEmpty()) {
            validationErrors.add(new FieldMessage("trailer", "The trailer field is required!"));
        } else if (!isVideo(trailer)) {
            validationErrors.add(new FieldMessage("trailer", "The trailer must be a valid video file!"));
        }
        if (video == null || video.isEmpty()) {
            validationErrors.add(new FieldMessage("video", "The video field is required!"));
        } else if (!isVideo(video)) {
            validationErrors.add(new FieldMessage("video", "The video must be a valid video file!"));
        }
        if (genres == null || genres.isEmpty()) {
            validationErrors.add(new FieldMessage("genres", "The genres field is required!"));
        }
        if (entertainmentTypeId == null) {
            validationErrors.add(new FieldMessage("entertainmentTypeId", "The entertainmentTypeId field is required!"));
        }
        if (duration == null) {
            validationErrors.add(new FieldMessage("duration", "The duration field is required!"));
        }
        if (launch == null) {
            validationErrors.add(new FieldMessage("launch", "The launch date field is required!"));
        } else if (launch.isAfter(LocalDate.now())) {
            validationErrors.add(new FieldMessage("launch", "The launch date cannot be a future date!"));
        }

        return validationErrors;
    }

    private ResponseEntity<ValidationError> createValidationErrorResponse(List<FieldMessage> validationErrors) {
        ValidationError errorsResponse = new ValidationError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                "Error in the validation of the fields",
                "/movie/add"
        );

        for (FieldMessage error : validationErrors) {
            errorsResponse.addError(error.getFieldName(), error.getMessage());
        }

        return ResponseEntity.badRequest().body(errorsResponse);
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("image/"));
    }

    private boolean isVideo(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("video/"));
    }

}
