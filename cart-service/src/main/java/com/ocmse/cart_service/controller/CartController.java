package com.ocmse.cart_service.controller;

import com.ocmse.cart_service.dto.CartItemRequest;
import com.ocmse.cart_service.dto.CartItemResponse;
import com.ocmse.cart_service.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addItem(@RequestBody CartItemRequest request, @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.addItem(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getUserCart(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.getUserCart(userId));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeItem(@PathVariable UUID cartId) {
        service.removeItem(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> calculateTotal(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.calculateTotal(userId));
    }
}