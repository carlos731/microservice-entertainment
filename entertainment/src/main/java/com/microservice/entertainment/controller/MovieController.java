package com.microservice.entertainment.controller;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.dto.MovieDTO;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.response.EntertainmentMovieResponse;
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
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UploadService uploadService;
    ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<MovieResponse> create(
            //@Valid @RequestBody MovieRequest request,
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
            @RequestParam(name = "duration", required = false) int duration
    ) throws IOException {
        // validar

        // Upload do arquivo
        String posterImgUrl = uploadService.upload(posterImg);
        String coverImgUrl = uploadService.upload(coverImg);
        String trailerUrl = uploadService.upload(trailer);
        String videoUrl = uploadService.upload(video);

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
        //entertainmentDTO.setLaunch(launch); recebo erro de esperar data e vem string
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

        // return link
        // return new ResponseEntity<>(videoUrl, HttpStatus.OK);

        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

}
