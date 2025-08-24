package com.ocmse.payment_service.controller;

import com.ocmse.payment_service.dto.PaymentInitResponse;
import com.ocmse.payment_service.dto.PaymentRequest;
import com.ocmse.payment_service.dto.PaymentResponse;
import com.ocmse.payment_service.dto.VerifyPaymentRequest;
import com.ocmse.payment_service.model.Payment;
import com.ocmse.payment_service.service.PaymentService;
import com.ocmse.payment_service.service.RazorpayService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RazorpayService razorpayService;

    public PaymentController(PaymentService paymentService, RazorpayService razorpayService) {
        this.paymentService = paymentService;
        this.razorpayService = razorpayService;
    }

    /** Step 1: Create order (client will open Razorpay Checkout with these details) */
    @PostMapping("/buy")
    public PaymentInitResponse buy(@Valid @RequestBody PaymentRequest req) throws Exception {
        Payment p = paymentService.initPayment(req);
        return new PaymentInitResponse(
                p.getPaymentUuid(),
                razorpayService.getPublicKey(),
                p.getRazorpayOrderId(),
                p.getAmount(),
                p.getCurrency()
        );
    }

    /** Step 2: Frontend calls after success handler of Razorpay Checkout */
    @PostMapping("/verify")
    public PaymentResponse verify(@Valid @RequestBody VerifyPaymentRequest req) {
        Payment p = paymentService.verifyAndCapture(req);
        return new PaymentResponse(
                p.getPaymentUuid(),
                p.getRazorpayOrderId(),
                p.getRazorpayPaymentId(),
                p.getStatus(),
                p.getAmount(),
                p.getCurrency()
        );
    }

    /** Check ownership (your requested endpoint) */
    @GetMapping("/verify-purchase")
    public boolean verifyPurchase(@RequestParam UUID userId, @RequestParam UUID productId) {
        return paymentService.hasPurchased(userId, productId);
    }

    /** Get by internal payment UUID (useful for support/refunds) */
    @GetMapping("/{paymentUuid}")
    public PaymentResponse getPayment(@PathVariable UUID paymentUuid) {
        Payment p = paymentService.getByUuid(paymentUuid);
        return new PaymentResponse(
                p.getPaymentUuid(), p.getRazorpayOrderId(), p.getRazorpayPaymentId(),
                p.getStatus(), p.getAmount(), p.getCurrency()
        );
    }
}
