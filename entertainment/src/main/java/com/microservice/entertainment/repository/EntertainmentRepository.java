package com.microservice.entertainment.repository;

import com.microservice.entertainment.models.entity.Entertainment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    @Query(value = "SELECT DISTINCT e.* " +
            "FROM tb_entertainment e " +
            "JOIN tb_genre_entertainment ge ON e.entertainment_id = ge.entertainment_id " +
            "WHERE e.title ILIKE %:title% " +
            "AND (:entertainmentTypeIds IS NULL OR e.entertainment_type_id IN :entertainmentTypeIds) " +
            "AND (:genreIds IS NULL OR ge.genre_id IN :genreIds) " +
            "AND (:years IS NULL OR EXTRACT(YEAR FROM e.launch) IN :years) " +
            "AND (:isVisibility IS NULL OR e.is_visibility = :isVisibility)",
            nativeQuery = true)
    Page<Entertainment> findByFilters(
            @Param("years") List<Integer> years,
            @Param("entertainmentTypeIds") List<Long> entertainmentTypeIds,
            @Param("genreIds") List<Long> genreIds,
            @Param("title") String title,
            @Param("isVisibility") Boolean isVisibility,
            Pageable pageable
    );

    @Query(value = "SELECT DISTINCT e.* FROM tb_entertainment e " + "WHERE e.title ILIKE %?1%", nativeQuery = true)
    Page<Entertainment> findByTitle(
            @Param("title") String title,
            Pageable pageable
    );
}
