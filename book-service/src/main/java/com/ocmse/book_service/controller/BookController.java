package com.ocmse.book_service.controller;

import com.ocmse.book_service.dto.BookRequest;
import com.ocmse.book_service.dto.BookResponse;
import com.ocmse.book_service.dto.PriceResponse;
import com.ocmse.book_service.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) { this.bookService = bookService; }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request, @RequestHeader("X-User-Id") UUID userId, @RequestHeader("X-User-Role") String role) {
        return ResponseEntity.ok(bookService.createBook(request, userId, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(bookService.getBook(id,userId));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(Pageable pageable, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id, @RequestHeader("X-User-Role") String role) {
        bookService.deleteBook(id, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam String query,
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(bookService.searchBooks(query, pageable));
    }

    @GetMapping("/get-price/{bookId}")
    public ResponseEntity<PriceResponse> getPrice(@PathVariable UUID bookId ){
        return ResponseEntity.ok(bookService.getPrice(bookId)) ;
    }
}