package com.ocmse.payment_service.dto;

import com.ocmse.payment_service.model.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentResponse {
    private UUID paymentUuid;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private PaymentStatus status;
    private BigDecimal amount;
    private String currency;

    public PaymentResponse() {}
    public PaymentResponse(UUID paymentUuid, String orderId, String paymentId, PaymentStatus status, BigDecimal amount, String currency) {
        this.paymentUuid = paymentUuid;
        this.razorpayOrderId = orderId;
        this.razorpayPaymentId = paymentId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
    }

    public UUID getPaymentUuid() { return paymentUuid; }
    public void setPaymentUuid(UUID paymentUuid) { this.paymentUuid = paymentUuid; }
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
