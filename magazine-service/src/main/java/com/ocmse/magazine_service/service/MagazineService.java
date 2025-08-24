package com.ocmse.magazine_service.service;

import com.ocmse.magazine_service.dto.MagazineRequest;
import com.ocmse.magazine_service.dto.MagazineResponse;
import com.ocmse.magazine_service.dto.PriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MagazineService {
    MagazineResponse createMagazine(MagazineRequest request, UUID userId, String role);
    MagazineResponse getMagazine(UUID id, UUID userId);
    Page<MagazineResponse> getAllMagazines(Pageable pageable, UUID userId);
    void deleteMagazine(UUID id, String role);
    Page<MagazineResponse> searchMagazines(String query, Pageable pageable);
    boolean checkIfUserHasPurchased(UUID magazineId, UUID userId) ;
    PriceResponse getPrice(UUID magazineId) ;
}