package com.ocmse.book_service.service;

import com.ocmse.book_service.dto.BookRequest;
import com.ocmse.book_service.dto.BookResponse;
import com.ocmse.book_service.dto.PriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookResponse createBook(BookRequest request, UUID userId, String role);
    BookResponse getBook(UUID id, UUID userId);
    Page<BookResponse> getAllBooks(Pageable pageable, UUID userId);
    void deleteBook(UUID id, String role);
    Page<BookResponse> searchBooks(String query, Pageable pageable);
    boolean checkIfUserHasPurchased(UUID bookId, UUID userId) ;
    PriceResponse getPrice(UUID bookId) ;
}