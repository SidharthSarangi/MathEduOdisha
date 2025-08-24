package com.ocmse.cart_service.repository;

import com.ocmse.cart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUserId(UUID userId);
    Optional<CartItem> findByUserIdAndProductId(UUID userId, UUID productId);
}