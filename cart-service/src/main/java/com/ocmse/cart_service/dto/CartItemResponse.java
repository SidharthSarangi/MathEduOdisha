package com.ocmse.cart_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemResponse {
    private UUID cartId;
    private UUID productId;
    private String productType;
    private BigDecimal price;

    public UUID getCartId() { return cartId; }
    public void setCartId(UUID cartId) { this.cartId = cartId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
