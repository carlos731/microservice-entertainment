package com.microservice.entertainment.repository;

import com.microservice.entertainment.models.dto.CastDTO;
import com.microservice.entertainment.models.entity.Cast;
import com.microservice.entertainment.models.response.CastPersonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastRepository extends JpaRepository<Cast, Long> {
    @Query(value = "SELECT c.cast_id AS cast_id, p.person_id AS person_id, c.character AS character, p.name AS name, p.description AS description, p.country AS country, p.avatar AS avatar, c.profession AS profession " +
            "FROM tb_cast c " +
            "JOIN tb_person p ON c.person_id = p.person_id " +
            "WHERE c.entertainment_id = :entertainmentId",
            nativeQuery = true)
    List<Object[]> findCastByEntertainmentIdRaw(@Param("entertainmentId") Long entertainmentId);
}
