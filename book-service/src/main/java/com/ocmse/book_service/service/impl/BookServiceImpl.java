package com.ocmse.book_service.service.impl;

import com.ocmse.book_service.dto.BookRequest;
import com.ocmse.book_service.dto.BookResponse;
import com.ocmse.book_service.dto.PriceResponse;
import com.ocmse.book_service.exception.BookNotFoundException;
import com.ocmse.book_service.model.Book;
import com.ocmse.book_service.repository.BookRepository;
import com.ocmse.book_service.service.BookService;
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
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);


    private final BookRepository bookRepository;
    private final WebClient webClient;
    private final String paymentServiceUrl ;

    public BookServiceImpl(BookRepository bookRepository, WebClient.Builder webClientBuilder, @Value("${payment.service.url}") String paymentServiceUrl) {
        this.bookRepository = bookRepository;
        this.webClient = webClientBuilder.build();
        this.paymentServiceUrl = paymentServiceUrl ;
    }

    @Override
    public BookResponse createBook(BookRequest request, UUID userId, String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can create books.");
        }

        Book book = new Book();
        book.setCategory(request.getCategory());
        book.setId(UUID.randomUUID());
        book.setTitle(request.getTitle());
        book.setAuthorId(request.getAuthorId());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setPublishedDate(request.getPublishedDate());
        book.setPrice(request.getPrice());
        book.setFileUrl(request.getFileUrl());
        book.setCoverImageUrl(request.getCoverImageUrl());
        book.setDescription(request.getDescription());
        book.setPageCount(request.getPageCount());
        book.setAvailable(true);
        book.setProductType("Book");

        return mapToResponse(bookRepository.save(book), userId);
    }

    @Override
    public BookResponse getBook(UUID id, UUID userId) {
        logger.info(paymentServiceUrl) ;
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));

        return mapToResponse(book, userId);
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable, UUID userId) {
        return bookRepository.findAll(pageable)
                .map(this::mapToResponseForPublicView); // no purchase check
    }

    @Override
    public Page<BookResponse> searchBooks(String query, Pageable pageable) {
        return bookRepository.searchBooks(query, pageable)
                .map(this::mapToResponseForPublicView); // no purchase check
    }

    @Override
    public PriceResponse getPrice(UUID bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id"+ bookId)) ;
        PriceResponse p = new PriceResponse() ;
        p.setPrice(book.getPrice());
        return p ;
    }

    @Override
    public void deleteBook(UUID id, String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete books.");
        }

        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id " + id);
        }

        bookRepository.deleteById(id);
    }

    /**
     * Book details endpoint (e.g. /book/{id}) → Check purchase
     */
    private BookResponse mapToResponse(Book book, UUID userId) {
//        boolean hasPurchased = checkIfUserHasPurchased(book.getId(), userId);
        boolean hasPurchased = true ;
        BookResponse response = buildCommonBookResponse(book);
        response.setFileUrl(hasPurchased ? book.getFileUrl() : null);
        return response;
    }

    /**
     * Search & list endpoints → Don't call PaymentMS
     */
    private BookResponse mapToResponseForPublicView(Book book) {
        BookResponse response = buildCommonBookResponse(book);
        response.setFileUrl(null); // always hide unless detail view
        return response;
    }

    private BookResponse buildCommonBookResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setCategory(book.getCategory());
        response.setTitle(book.getTitle());
        response.setAuthorId(book.getAuthorId());
        response.setPublisher(book.getPublisher());
        response.setIsbn(book.getIsbn());
        response.setPublishedDate(book.getPublishedDate());
        response.setPrice(book.getPrice());
        response.setCoverImageUrl(book.getCoverImageUrl());
        response.setDescription(book.getDescription());
        response.setPageCount(book.getPageCount());
        response.setAvailable(book.getAvailable());
        response.setProductType(book.getProductType());
        return response;
    }

    @CircuitBreaker(name = "paymentServiceCB", fallbackMethod = "fallbackPurchaseCheck")
    public boolean checkIfUserHasPurchased(UUID bookId, UUID userId) {
        return webClient.get()
                .uri(paymentServiceUrl + "?userId=" + userId + "&productId=" + bookId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block(); // safe here as it's inside service layer
    }

    private boolean fallbackPurchaseCheck(UUID bookId, UUID userId, Throwable t) {
        // Fallback: assume not purchased
        return false;
    }
}
