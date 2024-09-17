package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.EntertainmentDTO;
import com.microservice.entertainment.models.entity.Genre;
import com.microservice.entertainment.models.request.EntertainmentFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntertainmentService {
    public EntertainmentDTO create(EntertainmentDTO dto);
    public EntertainmentDTO update(Long id, EntertainmentDTO dto);
    public List<EntertainmentDTO> findAll(Pageable pageable);
    public Page<EntertainmentDTO> findByFilters(Pageable pageable, EntertainmentFilterRequest request);
    public Page<EntertainmentDTO> search(Pageable pageable, EntertainmentFilterRequest request);
    public Optional<EntertainmentDTO> findById(Long id);
    public void delete(Long id);
}
