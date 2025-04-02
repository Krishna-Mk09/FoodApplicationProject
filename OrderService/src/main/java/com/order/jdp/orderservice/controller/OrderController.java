package com.order.jdp.orderservice.controller;

import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping( value = "/createOrder" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Order createOrder(@RequestBody Order order) {
        log.info("OrderController.createOrder");
        Order saveOrder = null;
        try {
            log.info("OrderController.createOrder: order: {}", order);
            saveOrder = orderService.createOrder(order);
            log.info("OrderController.createOrder: saveOrder: {}", saveOrder);
        } catch (Exception e) {
            log.error("OrderController.createOrder: Exception: {}", e.getStackTrace());
            throw new RuntimeException(e);
        }

        return saveOrder;
    }

    @PostMapping("/updateOrder")
    public void updateOrder() {
        log.info("OrderController.updateOrder");
        orderService.updateOrder();
    }

    @PostMapping("/cancelOrder")
    public void cancelOrder(@RequestBody OrderDTO orderDTO) {
        try {
            log.info("OrderController.cancelOrder");
            orderService.cancelOrder(orderDTO);
            log.info("OrderController.cancelOrder: Order cancelled successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/getOrderDetails")
    public List<OrderItemsDTO> getOrderDetails(@RequestBody OrderDTO orderDTO) {
        List<OrderItemsDTO> orderDetails = new ArrayList<>();
        try{
            log.info("OrderController.getOrderDetails");
             orderDetails = orderService.getOrderDetails(orderDTO);
            log.info("OrderController.getOrderDetails: Order details fetched successfully");

        }catch (Exception e) {
            log.error("OrderController.getOrderDetails: Exception: {}", e.getStackTrace());
            throw new RuntimeException(e);
        }
        return orderDetails;
    }

    @GetMapping(value = "/getOrder/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDTO> getOrder(@PathVariable("userId") long userId) {
        log.info("OrderController.getOrder: userId={}", userId);

        try {
            List<OrderDTO> order = orderService.getAllOrders(userId);
            log.info("OrderController.getOrder: order: {}", order);
            return order;
        } catch (Exception e) {
            log.error("OrderController.getOrder: Exception: ", e);
            throw new RuntimeException(e);
        }
    }


}
