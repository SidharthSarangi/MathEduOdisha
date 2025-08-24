package com.ocmse.cart_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
public class CartItem {

    @Id
    @Column(name = "cart_id", columnDefinition = "UUID")
    private UUID cartId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productType; // BOOK or MAGAZINE

    @Column(nullable = false)
    private BigDecimal price; // fetched from respective service

    // Getters & Setters
    public UUID getCartId() { return cartId; }
    public void setCartId(UUID cartId) { this.cartId = cartId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
