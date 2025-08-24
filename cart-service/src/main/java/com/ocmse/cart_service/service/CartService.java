package com.ocmse.cart_service.service;

import com.ocmse.cart_service.dto.CartItemRequest;
import com.ocmse.cart_service.dto.CartItemResponse;
import com.ocmse.cart_service.dto.ProductResponse;
import com.ocmse.cart_service.model.CartItem;
import com.ocmse.cart_service.repository.CartRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository repo;
    private final WebClient webClient;
    private final String bookServiceUrl ;
    private final String magazineServiceUrl ;

    public CartService(CartRepository repo, WebClient.Builder webClientBuilder,@Value("${book.service.url}") String bookServiceUrl,
                       @Value("${magazine.service.url}") String magazineServiceUrl) {
        this.repo = repo;
        this.webClient = webClientBuilder.build() ;
        this.bookServiceUrl = bookServiceUrl ;
        this.magazineServiceUrl = magazineServiceUrl ;
    }

    public CartItemResponse addItem(CartItemRequest request, UUID userId) {
        Optional<CartItem> existingItem = repo.findByUserIdAndProductId(userId, request.getProductId());

        if (existingItem.isPresent()) {
            throw new IllegalStateException("Product already exists in cart for this user.");
        }

        BigDecimal price = fetchProductPrice(request.getProductId(), request.getProductType());

        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(request.getProductId());
        item.setProductType(request.getProductType());
        item.setCartId(UUID.randomUUID());
        item.setPrice(price);

        CartItem saved = repo.save(item);
        return toResponse(saved);
    }


    public List<CartItemResponse> getUserCart(UUID userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void removeItem(UUID cartId) {
        if (!repo.existsById(cartId)) {
            throw new NoSuchElementException("Cart item with ID " + cartId + " not found.");
        }
        repo.deleteById(cartId);
    }


    public BigDecimal calculateTotal(UUID userId) {
        List<CartItem> items = repo.findByUserId(userId);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(item.getPrice());
        }

        return total;
    }


    private CartItemResponse toResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setCartId(item.getCartId());
        response.setProductId(item.getProductId());
        response.setProductType(item.getProductType());
        response.setPrice(item.getPrice());
        return response;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackFetchPrice")
    private BigDecimal fetchProductPrice(UUID productId, String productType) {
        String url;

        if ("BOOK".equalsIgnoreCase(productType)) {
            url = bookServiceUrl + productId;
        } else if ("MAGAZINE".equalsIgnoreCase(productType)) {
            url = magazineServiceUrl + productId;
        } else {
            throw new IllegalArgumentException("Invalid product type: " + productType);
        }

        ProductResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        Mono.error(new RuntimeException("Failed with status: " + clientResponse.statusCode()))
                )
                .bodyToMono(ProductResponse.class)
                .block();

        if (response == null || response.getPrice() == null) {
            throw new IllegalStateException("Price missing in response for productId: " + productId);
        }

        return response.getPrice();
    }

    private BigDecimal fallbackFetchPrice(UUID productId, String productType, Throwable ex) {
        // Log the fallback reason
        System.err.println("Fallback triggered for productId=" + productId + " due to: " + ex.getMessage());

        // Return a default price, or throw custom exception if needed
        return BigDecimal.ZERO;
    }

}