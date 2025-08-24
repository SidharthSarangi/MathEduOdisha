package com.ocmse.book_service.repository;

import com.ocmse.book_service.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.productType) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Book> searchBooks(@Param("query") String query, Pageable pageable);
}