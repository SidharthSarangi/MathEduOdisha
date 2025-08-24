package com.ocmse.payment_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentInitResponse {
    private UUID paymentUuid;
    private String razorpayKey;     // public key for client
    private String razorpayOrderId;
    private BigDecimal amount;      // rupees
    private String currency;

    public PaymentInitResponse() {}

    public PaymentInitResponse(UUID paymentUuid, String razorpayKey, String razorpayOrderId, BigDecimal amount, String currency) {
        this.paymentUuid = paymentUuid;
        this.razorpayKey = razorpayKey;
        this.razorpayOrderId = razorpayOrderId;
        this.amount = amount;
        this.currency = currency;
    }
    public UUID getPaymentUuid() { return paymentUuid; }
    public void setPaymentUuid(UUID paymentUuid) { this.paymentUuid = paymentUuid; }
    public String getRazorpayKey() { return razorpayKey; }
    public void setRazorpayKey(String razorpayKey) { this.razorpayKey = razorpayKey; }
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
