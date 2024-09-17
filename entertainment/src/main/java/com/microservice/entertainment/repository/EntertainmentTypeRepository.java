package com.microservice.entertainment.repository;

import com.microservice.entertainment.models.entity.EntertainmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainmentTypeRepository extends JpaRepository<EntertainmentType, Long> {
    EntertainmentType findEntertainmentTypeById(Long id);
}
