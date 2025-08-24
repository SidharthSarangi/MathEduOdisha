package com.ocmse.search_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ocmse.search_service.dto.BookDto;
import com.ocmse.search_service.dto.MagazineDto;
import com.ocmse.search_service.dto.PageResponse;
import com.ocmse.search_service.dto.SearchResultDto;
import com.ocmse.search_service.service.SearchService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final WebClient webClient;
    private final String bookServiceUrl;
    private final String magazineServiceUrl;

    public SearchServiceImpl(WebClient.Builder webClientBuilder,
                             @Value("${book.service.url}") String bookServiceUrl,
                             @Value("${magazine.service.url}") String magazineServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.bookServiceUrl = bookServiceUrl;
        this.magazineServiceUrl = magazineServiceUrl;
    }

    @CircuitBreaker(name = "bookServiceCB", fallbackMethod = "fallbackBooks")
    public Mono<List<BookDto>> fetchBooks(String query, int page, int size) {
        return webClient.get()
                .uri(bookServiceUrl + "?query=" + query + "&page=" + page + "&size=" + size)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> logger.info("Raw book-service response: {}", json))
                .map(this::parseBookListFromJson)
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(throwable -> {
                    // Only catch and fallback for low-level, expected issues
                    if (throwable instanceof WebClientRequestException ||
                            throwable instanceof java.net.UnknownHostException) {
                        logger.warn("DNS/network error, triggering manual fallback", throwable);
                        return fallbackBooks(query, page, size, throwable);
                    }
                    // Let circuit breaker handle other exceptions
                    return Mono.error(throwable);
                });
    }


    @CircuitBreaker(name = "magazineServiceCB", fallbackMethod = "fallbackMagazines")
    public Mono<List<MagazineDto>> fetchMagazines(String query, int page, int size) {
        return webClient.get()
                .uri(magazineServiceUrl + "?query=" + query + "&page=" + page + "&size=" + size)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> logger.info("Raw magazine-service response: {}", json))
                .map(this::parseMagazineListFromJson)
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(throwable -> {
                    // Only catch and fallback for low-level, expected issues
                    if (throwable instanceof WebClientRequestException ||
                            throwable instanceof java.net.UnknownHostException) {
                        logger.warn("DNS/network error, triggering manual fallback", throwable);
                        return fallbackMagazines(query, page, size, throwable);
                    }
                    // Let circuit breaker handle other exceptions
                    return Mono.error(throwable);
                });
    }

    @Override
    public Mono<List<SearchResultDto>> search(String query, int page, int size) {
        if (query == null || query.trim().isEmpty()) {
            return Mono.just(Collections.emptyList());
        }
        logger.info(bookServiceUrl);
        logger.info(magazineServiceUrl) ;
        // Book service call
        Mono<List<BookDto>> booksMono = fetchBooks(query,page,size) ;
        // Magazine service call
        Mono<List<MagazineDto>> magazinesMono = fetchMagazines(query,page,size) ;

        // Combine both results
        return Mono.zip(booksMono, magazinesMono)
                .map(tuple -> {
                    List<BookDto> books = tuple.getT1();
                    List<MagazineDto> magazines = tuple.getT2();
                    List<SearchResultDto> results = new ArrayList<>();
                    logger.info("Fetched {} books and {} magazines", books.size(), magazines.size());

                    // Convert books
                    for (BookDto b : books) {
                        SearchResultDto dto = new SearchResultDto();
                        dto.setCategory(b.getProductType());
                        dto.setId(b.getId());
                        dto.setTitle(b.getTitle());
                        dto.setCreator(b.getAuthorId());
                        dto.setPublisher(b.getPublisher());
                        dto.setIdentifier(b.getIsbn());
                        dto.setDescription(b.getDescription());
                        dto.setPrice(b.getPrice());
                        dto.setFileUrl(b.getFileUrl());
                        dto.setCoverImageUrl(b.getCoverImageUrl());
                        results.add(dto);
                    }

                    // Convert magazines
                    for (MagazineDto m : magazines) {
                        SearchResultDto dto = new SearchResultDto();
                        dto.setCategory(m.getProductType());
                        dto.setId(m.getId());
                        dto.setTitle(m.getTitle());
                        dto.setCreator(m.getAuthorId());
                        dto.setPublisher(m.getPublisher());
                        dto.setIdentifier(m.getIssn());
                        dto.setDescription(m.getDescription());
                        dto.setPrice(m.getPrice());
                        dto.setFileUrl(m.getFileUrl());
                        dto.setCoverImageUrl(m.getCoverImageUrl());
                        results.add(dto);
                    }

                    return results;
                })
                .onErrorResume(e -> {
                    logger.error("Error during search aggregation", e) ;
                    return Mono.just(Collections.emptyList()) ;
                });
    }

    private List<MagazineDto> parseMagazineListFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // ✅ Enables LocalDate support
            PageResponse<MagazineDto> response = mapper.readValue(
                    json,
                    mapper.getTypeFactory().constructParametricType(PageResponse.class, MagazineDto.class)
            );
            return response.getContent();
        } catch (Exception e) {
            logger.error("Error parsing MagazineDto from JSON:\n{}\n{}", json, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<BookDto> parseBookListFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // ✅ Enables LocalDate support
            PageResponse<BookDto> response = mapper.readValue(
                    json,
                    mapper.getTypeFactory().constructParametricType(PageResponse.class, BookDto.class)
            );
            return response.getContent();
        } catch (Exception e) {
            logger.error("Error parsing BookDto from JSON:\n{}\n{}", json, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    // Fallbacks for circuit breaker
    public Mono<List<BookDto>> fallbackBooks(String query, int page, int size, Throwable t) {
        logger.error("Fallback for books triggered due to: {}", t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<List<MagazineDto>> fallbackMagazines(String query, int page, int size, Throwable t) {
        logger.error("Fallback for magazines triggered due to: {}", t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }


}
