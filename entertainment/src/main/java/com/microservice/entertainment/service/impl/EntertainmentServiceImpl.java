package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.exception.CustomException;
import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.mapper.EntertainmentMapper;
import com.microservice.entertainment.models.request.EntertainmentFilterRequest;
import com.microservice.entertainment.repository.EntertainmentRepository;
import com.microservice.entertainment.service.EntertainmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntertainmentServiceImpl implements EntertainmentService {
    @Autowired
    private EntertainmentRepository repository;
    @Autowired
    private EntertainmentMapper mapper;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public EntertainmentDTO create(EntertainmentDTO dto) {
        Entertainment entity = modelMapper.map(dto, Entertainment.class);
        entity.setCreated(LocalDateTime.now());
        entity = repository.save(entity);
        return modelMapper.map(entity, EntertainmentDTO.class);
    }

    @Override
    public EntertainmentDTO update(Long id, EntertainmentDTO dto) {
        Entertainment entity = repository.findById(id).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND
        ));

        dto.setId(id);
        dto.setCreated(entity.getCreated());
        dto.setUpdated(LocalDateTime.now());

        entity = mapper.mapDtoToEntity(dto);
        entity = repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<EntertainmentDTO> findAll(Pageable pageable) {
        Page<Entertainment> results = repository.findAll(pageable);
        return results.stream().map(mapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<EntertainmentDTO> findByFilters(Pageable pageable, EntertainmentFilterRequest request) {
        Page<Entertainment> results = repository.findByFilters(
                request.getYears(),
                request.getEntertainmentTypeIds(),
                request.getGenreIds(),
                request.getTitle(),
                request.getIsVisibility(),
                pageable
        );
        return results.map(mapper::mapEntityToDto);
    }

    @Override
    public Page<EntertainmentDTO> search(Pageable pageable, EntertainmentFilterRequest request) {
        Page<Entertainment> results = repository.findByFilters(
                request.getYears(),
                request.getEntertainmentTypeIds(),
                request.getGenreIds(),
                request.getTitle(),
                request.getIsVisibility(),
                pageable
        );
        return results.map(mapper::mapEntityToDto);
    }

    @Override
    public Optional<EntertainmentDTO> findById(Long id) {
        Optional<Entertainment> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return result.map(mapper::mapEntityToDto);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
