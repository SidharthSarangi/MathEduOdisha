package com.ocmse.magazine_service.repository;

import com.ocmse.magazine_service.model.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MagazineRepository extends JpaRepository<Magazine, UUID> {
    @Query("SELECT m FROM Magazine m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.productType) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.publisher) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.issn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Magazine> searchMagazines(@Param("query") String query, Pageable pageable);
}