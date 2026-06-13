package com.antiguedades.antique;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface AntiqueRepository extends JpaRepository<Antique, UUID> {
    List<Antique> findByCatalogIdOrderByCreatedAtDesc(UUID catalogId);

    @Query("SELECT a FROM Antique a ORDER BY a.createdAt DESC")
    List<Antique> findAllOrderByCreatedAtDesc();

    @Query("SELECT a FROM Antique a WHERE " +
           "(:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(a.country) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(a.author) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:type IS NULL OR a.type = :type) " +
           "AND (:subcategory IS NULL OR a.subcategory = :subcategory) " +
           "AND (:detail IS NULL OR a.detail = :detail) " +
           "AND (:condition IS NULL OR a.condition = :condition) " +
           "ORDER BY a.createdAt DESC")
    List<Antique> search(@Param("search") String search,
                         @Param("type") AntiqueType type,
                         @Param("subcategory") String subcategory,
                         @Param("detail") String detail,
                         @Param("condition") String condition);

    long countByType(AntiqueType type);
    long countByCatalogId(UUID catalogId);
}
