package com.ocmse.payment_service.controller;

import com.ocmse.payment_service.model.PaymentStatus;
import com.ocmse.payment_service.repository.PaymentRepository;
import com.ocmse.payment_service.util.RazorpaySignatureUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/payments")
public class WebhookController {

    private final PaymentRepository repo;
    private final String webhookSecret;

    public WebhookController(PaymentRepository repo, @Value("${razorpay.webhook.secret}") String webhookSecret) {
        this.repo = repo;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(HttpServletRequest request, @RequestHeader("X-Razorpay-Signature") String signature) throws Exception {
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        if (!RazorpaySignatureUtil.verifyWebhookSignature(body, signature, webhookSecret)) {
            return ResponseEntity.status(400).body("Invalid webhook signature");
        }

        JSONObject event = new JSONObject(body);
        String eventType = event.optString("event");
        JSONObject payload = event.getJSONObject("payload");

        if ("payment.captured".equals(eventType)) {
            String orderId = payload.getJSONObject("payment").getJSONObject("entity").optString("order_id");
            String paymentId = payload.getJSONObject("payment").getJSONObject("entity").optString("id");

            repo.findByRazorpayOrderId(orderId).ifPresent(p -> {
                if (p.getStatus() != PaymentStatus.SUCCESS) {
                    p.setStatus(PaymentStatus.SUCCESS);
                    p.setRazorpayPaymentId(paymentId);
                    repo.save(p);
                }
            });
        }

        // You can handle other events if needed.
        return ResponseEntity.ok("ok");
    }
}