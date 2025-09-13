package com.payment.jdp.PaymentService.service;

import com.razorpay.Order;

public interface PaymentService {
    Order createOrder(int amount, String currency, String receipt) throws Exception;
    boolean verifyPayment(String orderId, String paymentId, String signature);

}
