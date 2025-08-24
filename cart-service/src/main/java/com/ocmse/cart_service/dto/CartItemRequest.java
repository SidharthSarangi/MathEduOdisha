package com.ocmse.cart_service.dto;

import java.util.UUID;

public class CartItemRequest {
    private UUID productId;
    private String productType; // BOOK or MAGAZINE

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
}