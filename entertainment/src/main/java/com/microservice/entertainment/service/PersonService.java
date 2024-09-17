package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    public PersonDTO create(PersonDTO dto);
    public PersonDTO update(Long id, PersonDTO dto);
    public List<PersonDTO> findAll();
    public Optional<PersonDTO> findById(Long id);
    public void delete(Long id);
}
