package com.payment.jdp.PaymentService.controller;

import com.payment.jdp.PaymentService.service.PaymentService;
import com.razorpay.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
 * Author Name : P.Ram Charan Teja
 * Date: 13-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam("amount") int amount, @RequestParam(value = "currency", defaultValue = "INR") String currency, @RequestParam(value = "receipt", defaultValue = "txn_123456") String receipt) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("🔵 Creating order | amount: {}, currency: {}, receipt: {}", amount, currency, receipt);

            Order order = paymentService.createOrder(amount, currency, receipt);

            log.info("🟢 Order created successfully | orderId: {}", Optional.ofNullable(order.get("id")));
            response.put("status", "success");
            response.put("orderId", order.get("id"));
            response.put("currency", order.get("currency"));
            response.put("amount", order.get("amount"));
        } catch (Exception e) {
            log.error("🔴 Error while creating order: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyPayment(@RequestParam("orderId") String orderId, @RequestParam("paymentId") String paymentId, @RequestParam("signature") String signature) {
        Map<String, Object> response = new HashMap<>();
        log.info("🔵 Verifying payment | orderId: {}, paymentId: {}", orderId, paymentId);
        boolean isValid = paymentService.verifyPayment(orderId, paymentId, signature);

        if (isValid) {
            log.info("🟢 Payment verified successfully for orderId: {}", orderId);
            response.put("status", "success");
            response.put("message", "Payment verified successfully!");
        } else {
            log.warn("🟠 Payment verification failed for orderId: {}", orderId);
            response.put("status", "error");
            response.put("message", "Payment verification failed!");
        }

        return response;
    }
}
