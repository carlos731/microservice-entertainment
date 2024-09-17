package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.exception.CustomException;
import com.microservice.entertainment.models.dto.PersonDTO;
import com.microservice.entertainment.models.entity.Person;
import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.mapper.PersonMapper;
import com.microservice.entertainment.repository.PersonRepository;
import com.microservice.entertainment.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository repository;
    @Autowired
    private PersonMapper mapper;

    @Override
    public PersonDTO create(PersonDTO dto) {
        Person entity = mapper.mapDtoToEntity(dto);
        entity = repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public PersonDTO update(Long id, PersonDTO dto) {
        Person entity = repository.findById(id).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND
        ));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAvatar(dto.getAvatar());
        entity.setCountry(dto.getCountry());
        entity.setProfession(dto.getProfession());
        entity = repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<PersonDTO> findAll() {
        List<Person> results = repository.findAll();
        return results.stream().map(mapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PersonDTO> findById(Long id) {
        Optional<Person> result = repository.findById(id);
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
