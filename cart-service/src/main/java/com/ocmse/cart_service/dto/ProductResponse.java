package com.ocmse.cart_service.dto;

import java.math.BigDecimal;

public class ProductResponse {
    private BigDecimal price;
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
