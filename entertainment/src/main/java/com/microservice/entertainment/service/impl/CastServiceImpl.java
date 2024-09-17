package com.microservice.entertainment.service.impl;

import com.microservice.entertainment.exception.CustomException;
import com.microservice.entertainment.models.dto.CastDTO;
import com.microservice.entertainment.models.entity.Cast;
import com.microservice.entertainment.models.enums.ErrorType;
import com.microservice.entertainment.models.enums.ProfessionType;
import com.microservice.entertainment.models.response.CastPersonResponse;
import com.microservice.entertainment.repository.CastRepository;
import com.microservice.entertainment.service.CastService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CastServiceImpl implements CastService {
    @Autowired
    private CastRepository repository;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public CastDTO create(CastDTO dto) {
        Cast entity = modelMapper.map(dto, Cast.class);
        entity = repository.save(entity);
        return modelMapper.map(entity, CastDTO.class);
    }

    @Override
    public CastDTO update(Long id, CastDTO dto) {
        Cast entity = repository.findById(id).orElseThrow(() -> new CustomException(
                ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND
        ));
        entity.setEntertainmentId(dto.getEntertainmentId());
        entity.setPersonId(dto.getPersonId());
        entity.setCharacter(dto.getCharacter());
        entity.setProfession(dto.getProfession());
        repository.save(entity);
        return dto;
    }

    @Override
    public List<CastDTO> findAll() {
        List<Cast> results = repository.findAll();
        return results.stream().map(result -> new ModelMapper().map(result, CastDTO.class)).collect((Collectors.toList()));
    }

    @Override
    public Optional<CastDTO> findById(Long id) {
        Optional<Cast> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        CastDTO castDTO = modelMapper.map(result.get(), CastDTO.class);
        return Optional.of(castDTO);
    }

    @Override
    public List<CastPersonResponse> findCastByEntertainmentId(Long entertainmentId) {
        List<Object[]> rawResults = repository.findCastByEntertainmentIdRaw(entertainmentId);
        return rawResults.stream()
                .map(result -> new CastPersonResponse(
                        ((Number) result[0]).longValue(), // cast_id
                        ((Number) result[1]).longValue(), // person_id
                        (String) result[2], // character
                        (String) result[3], // name
                        (String) result[5], // country
                        (String) result[6], // avatar
                        ProfessionType.toEnum(((Number) result[7]).intValue()) // profession
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException(ErrorType.NOT_FOUND, "Resource not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

}
