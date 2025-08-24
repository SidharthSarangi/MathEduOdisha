package com.ocmse.payment_service.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient client;
    private final String publicKey;

    public RazorpayService(@Value("${razorpay.key}") String key,
                           @Value("${razorpay.secret}") String secret,
                           @Value("${razorpay.key.public}") String publicKey) throws Exception {
        this.client = new RazorpayClient(key, secret);
        this.publicKey = publicKey;
    }

    public Order createOrder(long amountPaise, String currency, String receipt) throws Exception {
        JSONObject req = new JSONObject();
        req.put("amount", amountPaise);      // paise
        req.put("currency", currency);
        req.put("receipt", receipt);
        req.put("payment_capture", 1);       // auto-capture
        return client.Orders.create(req);
    }

    public String getPublicKey() {
        return publicKey;
    }
}
