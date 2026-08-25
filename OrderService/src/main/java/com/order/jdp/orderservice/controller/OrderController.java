package com.order.jdp.orderservice.controller;

import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.entity.RestaurantInfo;
import com.order.jdp.orderservice.service.OrderService;
import com.order.jdp.orderservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RestaurantService restaurantService;

    @GetMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantInfo> getAllRestaurants() {
        log.info("OrderController.getAllRestaurants");
        try {
            List<RestaurantInfo> restaurants = restaurantService.getAllRestaurants();
            log.info("OrderController.getAllRestaurants: found {} restaurants", restaurants.size());
            return restaurants;
        } catch (Exception e) {
            log.error("OrderController.getAllRestaurants: Exception: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/restaurants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantInfo> getRestaurantById(@PathVariable("id") Long id) {
        log.info("OrderController.getRestaurantById: id={}", id);
        try {
            return restaurantService.getRestaurantById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("OrderController.getRestaurantById: Exception: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/restaurants/{restaurantId}/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemsInfo> getRestaurantMenu(@PathVariable("restaurantId") Long restaurantId) {
        log.info("OrderController.getRestaurantMenu: restaurantId={}", restaurantId);
        try {
            List<ItemsInfo> menuItems = restaurantService.getRestaurantMenu(restaurantId);
            log.info("OrderController.getRestaurantMenu: found {} items", menuItems.size());
            return menuItems;
        } catch (Exception e) {
            log.error("OrderController.getRestaurantMenu: Exception: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/createOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order createOrder(@RequestBody Order order) {
        log.info("OrderController.createOrder");
        Order saveOrder;
        try {
            log.info("OrderController.createOrder: order: {}", order);
            saveOrder = orderService.createOrder(order);
            log.info("OrderController.createOrder: saveOrder: {}", saveOrder);
        } catch (Exception e) {
            log.error("OrderController.createOrder: Exception: {}", ExceptionUtils.getStackTrace(e));
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
        try {
            log.info("OrderController.getOrderDetails");
            orderDetails = orderService.getOrderDetails(orderDTO);
            log.info("OrderController.getOrderDetails: Order details fetched successfully");

        } catch (Exception e) {
            log.error("OrderController.getOrderDetails: Exception: {}", ExceptionUtils.getStackTrace(e));
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
