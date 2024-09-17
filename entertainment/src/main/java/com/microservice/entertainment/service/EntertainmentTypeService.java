package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.EntertainmentTypeDTO;

import java.util.List;
import java.util.Optional;

public interface EntertainmentTypeService {
    public EntertainmentTypeDTO create(EntertainmentTypeDTO dto);
    public EntertainmentTypeDTO update(Long id, EntertainmentTypeDTO dto);
    public List<EntertainmentTypeDTO> findAll();
    public Optional<EntertainmentTypeDTO> findById(Long id);
    public EntertainmentTypeDTO findEntertainmentTypeById(Long id);
    public void delete(Long id);
}
