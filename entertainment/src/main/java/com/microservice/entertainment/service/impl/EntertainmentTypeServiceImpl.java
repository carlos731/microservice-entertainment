package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.exception.CustomException;
import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;
import com.microservice.entertainment.models.entity.Entertainment;
import com.microservice.entertainment.models.entity.EntertainmentType;
import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.mapper.EntertainmentTypeMapper;
import com.microservice.entertainment.repository.EntertainmentTypeRepository;
import com.microservice.entertainment.service.EntertainmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntertainmentTypeServiceImpl implements EntertainmentTypeService {
    @Autowired
    private EntertainmentTypeRepository repository;

    @Autowired
    private EntertainmentTypeMapper mapper;

    @Override
    public EntertainmentTypeDTO create(EntertainmentTypeDTO dto) {
        EntertainmentType entity = mapper.mapDtoToEntity(dto);
        entity = repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public EntertainmentTypeDTO update(Long id, EntertainmentTypeDTO dto) {
        EntertainmentType entity = repository.findById(id).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND
        ));
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<EntertainmentTypeDTO> findAll() {
        List<EntertainmentType> results = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return results.stream().map(mapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<EntertainmentTypeDTO> findById(Long id) {
        Optional<EntertainmentType> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return result.map(mapper::mapEntityToDto);
    }

    @Override
    public EntertainmentTypeDTO findEntertainmentTypeById(Long id) {
        EntertainmentType entity = repository.findEntertainmentTypeById(id);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
