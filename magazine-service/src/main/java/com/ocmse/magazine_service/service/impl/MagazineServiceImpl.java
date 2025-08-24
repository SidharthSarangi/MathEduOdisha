package com.ocmse.magazine_service.service.impl;

import com.ocmse.magazine_service.dto.MagazineRequest;
import com.ocmse.magazine_service.dto.MagazineResponse;
import com.ocmse.magazine_service.dto.PriceResponse;
import com.ocmse.magazine_service.exception.MagazineNotFoundException;
import com.ocmse.magazine_service.model.Magazine;
import com.ocmse.magazine_service.repository.MagazineRepository;
import com.ocmse.magazine_service.service.MagazineService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@Service
public class MagazineServiceImpl implements MagazineService {

    private static final Logger logger = LoggerFactory.getLogger(MagazineServiceImpl.class);

    private final MagazineRepository repository;
    private final WebClient webClient;
    private final String paymentServiceUrl ;

    public MagazineServiceImpl(MagazineRepository repository, WebClient.Builder webClientBuilder, @Value("${payment.service.url}") String paymentServiceUrl) {

        this.repository = repository;
        this.webClient = webClientBuilder.build();
        this.paymentServiceUrl = paymentServiceUrl ;

    }

    @Override
    public MagazineResponse createMagazine(MagazineRequest request, UUID userId ,String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can create books.");
        }
        Magazine magazine = new Magazine();
        magazine.setCategory(request.getCategory());
        magazine.setId(UUID.randomUUID());
        magazine.setTitle(request.getTitle());
        magazine.setAuthorId(request.getAuthorId());
        magazine.setPublisher(request.getPublisher());
        magazine.setIssn(request.getIssn());
        magazine.setPublishedDate(request.getPublishedDate());
        magazine.setPrice(request.getPrice());
        magazine.setFileUrl(request.getFileUrl());
        magazine.setCoverImageUrl(request.getCoverImageUrl());
        magazine.setDescription(request.getDescription());
        magazine.setPageCount(request.getPageCount());
        magazine.setAvailable(true);
        magazine.setProductType("Magazine");
        return mapToResponse(repository.save(magazine), userId);
    }

    @Override
    public MagazineResponse getMagazine(UUID id, UUID userId) {
        logger.info(paymentServiceUrl) ;
        Magazine m = repository.findById(id)
                .orElseThrow(() -> new MagazineNotFoundException("Magazine not found with id " + id));
        return mapToResponse(m, userId) ;
    }

    @Override
    public Page<MagazineResponse> getAllMagazines(Pageable pageable, UUID userId) {
        return repository.findAll(pageable).map(this::mapToResponseForPublicView);
    }

    @Override
    public void deleteMagazine(UUID id, String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete magazines.");
        }
        if (!repository.existsById(id)) {
            throw new MagazineNotFoundException("Magazine not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public PriceResponse getPrice(UUID magazineId){
        Magazine m = repository.findById(magazineId)
                .orElseThrow(() -> new MagazineNotFoundException("Magazine not found with id"+ magazineId)) ;
        PriceResponse p = new PriceResponse() ;
        p.setPrice(m.getPrice());
        return p ;
    }

    public Page<MagazineResponse> searchMagazines(String query, Pageable pageable) {
        return repository.searchMagazines(query, pageable)
                .map(this::mapToResponseForPublicView);
    }

    private MagazineResponse mapToResponse(Magazine magazine, UUID userId){
//        boolean hasPurchased = checkIfUserHasPurchased(magazine.getId(), userId) ;
        boolean hasPurchased = true ;
        MagazineResponse response = buildCommonMagazineResponse(magazine) ;
        response.setFileUrl(hasPurchased ? magazine.getFileUrl() : null);
        return response ;
    }

    private MagazineResponse mapToResponseForPublicView(Magazine magazine){
        MagazineResponse response = buildCommonMagazineResponse(magazine) ;
        response.setFileUrl(null);
        return response ;
    }

    private MagazineResponse buildCommonMagazineResponse(Magazine magazine) {
        MagazineResponse response = new MagazineResponse();
        response.setId(magazine.getId());
        response.setCategory(magazine.getCategory());
        response.setTitle(magazine.getTitle());
        response.setAuthorId(magazine.getAuthorId());
        response.setPublisher(magazine.getPublisher());
        response.setIssn(magazine.getIssn());
        response.setPublishedDate(magazine.getPublishedDate());
        response.setPrice(magazine.getPrice());
        response.setCoverImageUrl(magazine.getCoverImageUrl());
        response.setDescription(magazine.getDescription());
        response.setPageCount(magazine.getPageCount());
        response.setAvailable(magazine.getAvailable());
        response.setProductType(magazine.getProductType());
        return response;
    }

    @CircuitBreaker(name = "paymentServiceCB", fallbackMethod = "fallbackPurchaseCheck")
    public boolean checkIfUserHasPurchased(UUID magazineId, UUID userId) {
        return webClient.get()
                .uri(paymentServiceUrl + "?userId=" + userId + "&productId=" + magazineId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block(); // safe here as it's inside service layer
    }

    private boolean fallbackPurchaseCheck(UUID magazineId, UUID userId, Throwable t) {
        // Fallback: assume not purchased
        return false;
    }

}
