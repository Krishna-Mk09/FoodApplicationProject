package com.order.jdp.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payment/create-order")
    Map<String, Object> createOrder(@RequestParam("amount") int amount,
                                    @RequestParam(value = "currency", defaultValue = "INR") String currency,
                                    @RequestParam(value = "receipt") String receipt);
}
