package com.ocmse.payment_service.repository;

import com.ocmse.payment_service.model.Payment;
import com.ocmse.payment_service.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentUuid(UUID paymentUuid);
    Optional<Payment> findByRazorpayOrderId(String orderId);
    boolean existsByUserIdAndProductIdAndStatus(UUID userId, UUID productId, PaymentStatus status);
}
