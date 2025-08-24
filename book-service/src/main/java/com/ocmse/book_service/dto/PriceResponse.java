package com.ocmse.book_service.dto;

import java.math.BigDecimal;

public class PriceResponse {
    private BigDecimal price ;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
