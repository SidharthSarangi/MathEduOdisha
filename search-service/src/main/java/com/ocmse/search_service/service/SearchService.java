package com.ocmse.search_service.service;

import com.ocmse.search_service.dto.SearchResultDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SearchService {
    Mono<List<SearchResultDto>> search(String query, int page, int size);
}
