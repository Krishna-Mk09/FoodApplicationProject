package com.payment.jdp.PaymentService.servceimp;

import com.payment.jdp.PaymentService.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final String keyId;
    private final String keySecret;

    public PaymentServiceImpl(
            @Value("${razorpay.key.id}") String keyId,
            @Value("${razorpay.key.secret}") String keySecret) throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
        this.keyId = keyId;
        this.keySecret = keySecret;
        log.info("🔹 RazorpayClient initialized with keyId: {}", keyId);
    }

    @Override
    public Order createOrder(int amount, String currency, String receipt) throws Exception {
        log.info("🔵 Creating Razorpay order | amount: {}, currency: {}, receipt: {}", amount, currency, receipt);

        JSONObject options = new JSONObject();
        options.put("amount", amount); // in paise
        options.put("currency", currency);
        options.put("receipt", receipt);

        Order order = razorpayClient.orders.create(options);
        log.info("🟢 Order created successfully | orderId: {}", Optional.ofNullable(order.get("id")));

        return order;
    }

    @Override
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        log.info("🔵 Verifying payment | orderId: {}, paymentId: {}", orderId, paymentId);

        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(attributes, keySecret);

            if (isValid) {
                log.info("🟢 Payment signature verified successfully for orderId: {}", orderId);
            } else {
                log.warn("🟠 Payment signature verification failed for orderId: {}", orderId);
            }

            return isValid;
        } catch (Exception e) {
            log.error("🔴 Error during payment verification: {}", e.getMessage(), e);
            return false;
        }
    }
}
