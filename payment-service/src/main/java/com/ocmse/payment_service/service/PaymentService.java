package com.ocmse.payment_service.service;

import com.ocmse.payment_service.dto.PaymentRequest;
import com.ocmse.payment_service.dto.VerifyPaymentRequest;
import com.ocmse.payment_service.model.Payment;
import com.ocmse.payment_service.model.PaymentStatus;
import com.ocmse.payment_service.repository.PaymentRepository;
import com.ocmse.payment_service.util.RazorpaySignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final RazorpayService razorpayService;
    private final String razorpaySecret;

    public PaymentService(PaymentRepository repo,
                          RazorpayService razorpayService,
                          @Value("${razorpay.secret}") String razorpaySecret) {
        this.repo = repo;
        this.razorpayService = razorpayService;
        this.razorpaySecret = razorpaySecret;
    }

    public Payment initPayment(PaymentRequest req) throws Exception {
        UUID paymentUuid = UUID.randomUUID();
        long paise = req.getAmount().multiply(new BigDecimal("100")).longValueExact();
        var order = razorpayService.createOrder(paise, req.getCurrency(), paymentUuid.toString());

        Payment p = new Payment();
        p.setPaymentUuid(paymentUuid);
        p.setUserId(req.getUserId());
        p.setProductId(req.getProductId());
        p.setCurrency(req.getCurrency());
        p.setAmount(req.getAmount());
        p.setStatus(PaymentStatus.CREATED);
        p.setRazorpayOrderId(order.get("id"));
        return repo.save(p);
    }

    public Payment verifyAndCapture(VerifyPaymentRequest dto) {
        // find by orderId created earlier
        Payment p = repo.findByRazorpayOrderId(dto.getRazorpayOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown orderId"));

        // sanity-check same user/product
        if (!p.getUserId().equals(dto.getUserId()) || !p.getProductId().equals(dto.getProductId())) {
            throw new IllegalArgumentException("Mismatched userId/productId for this order");
        }

        // verify signature
        boolean ok = RazorpaySignatureUtil.verifyPaymentSignature(
                dto.getRazorpayOrderId(),
                dto.getRazorpayPaymentId(),
                dto.getRazorpaySignature(),
                razorpaySecret
        );
        if (!ok) {
            p.setStatus(PaymentStatus.FAILED);
            p.setRazorpayPaymentId(dto.getRazorpayPaymentId());
            p.setRazorpaySignature(dto.getRazorpaySignature());
            repo.save(p);
            throw new IllegalArgumentException("Signature verification failed");
        }

        p.setRazorpayPaymentId(dto.getRazorpayPaymentId());
        p.setRazorpaySignature(dto.getRazorpaySignature());
        p.setStatus(PaymentStatus.SUCCESS);
        return repo.save(p);
    }

    /** For your use-case: check if a user already owns a product */
    public boolean hasPurchased(UUID userId, UUID productId) {
        return repo.existsByUserIdAndProductIdAndStatus(userId, productId, PaymentStatus.SUCCESS);
    }

    public Payment getByUuid(UUID paymentUuid) {
        return repo.findByPaymentUuid(paymentUuid)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}
