package com.microservice.entertainment.service;

import com.microservice.entertainment.models.dto.CastDTO;
import com.microservice.entertainment.models.response.CastPersonResponse;

import java.util.List;
import java.util.Optional;

public interface CastService {
    public CastDTO create(CastDTO dto);
    public CastDTO update(Long id, CastDTO dto);
    public List<CastDTO> findAll();
    public Optional<CastDTO> findById(Long id);
    public List<CastPersonResponse> findCastByEntertainmentId(Long entertainmentId);
    public void delete(Long id);
}
