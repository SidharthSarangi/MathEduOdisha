package com.ocmse.payment_service.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class RazorpaySignatureUtil {

    private RazorpaySignatureUtil() {}

    public static String hmacSHA256(String data, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Error generating HMAC", e);
        }
    }

    public static boolean verifyPaymentSignature(String orderId, String paymentId, String expectedSignature, String secret) {
        String payload = orderId + '|' + paymentId;
        String actual = hmacSHA256(payload, secret);
        return actual.equals(expectedSignature);
    }

    public static boolean verifyWebhookSignature(String payload, String headerSignature, String webhookSecret) {
        String actual = hmacSHA256(payload, webhookSecret);
        return actual.equals(headerSignature);
    }
}
