package com.ocmse.magazine_service.controller;

import com.ocmse.magazine_service.dto.MagazineRequest;
import com.ocmse.magazine_service.dto.MagazineResponse;
import com.ocmse.magazine_service.dto.PriceResponse;
import com.ocmse.magazine_service.service.MagazineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/magazines")
public class MagazineController {

    private final MagazineService service;

    public MagazineController(MagazineService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MagazineResponse> createMagazine(@Valid @RequestBody MagazineRequest request, @RequestHeader("X-User-Id") UUID userId, @RequestHeader("X-User-Role") String role) {
        return ResponseEntity.ok(service.createMagazine(request, userId, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MagazineResponse> getMagazine(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.getMagazine(id, userId));
    }

    @GetMapping
    public ResponseEntity<Page<MagazineResponse>> getAllMagazines(Pageable pageable, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.getAllMagazines(pageable, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazine(@PathVariable UUID id, @RequestHeader("X-User-Role") String role) {
        service.deleteMagazine(id, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MagazineResponse>> searchMagazines(
            @RequestParam String query,
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(service.searchMagazines(query, pageable));
    }

    @GetMapping("/get-price/{magazineId}")
    public ResponseEntity<PriceResponse> getPrice(@PathVariable UUID magazineId ){
        return ResponseEntity.ok(service.getPrice(magazineId)) ;
    }
}
