package com.order.jdp.orderservice.serviceimpl;

import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.entity.OrderItems;
import com.order.jdp.orderservice.repository.OrderItemsRepository;
import com.order.jdp.orderservice.repository.OrderRepository;
import com.order.jdp.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public List<OrderDTO> getAllOrders(long userId) {
        try {
            log.info("OrderController.getAllOrders");
            List<Order> orders = orderRepository.findByUserId(userId);
            log.info("OrderController.getAllOrders: orders: {}", orders);
            if (orders != null && !orders.isEmpty()) {
                return orders.stream().map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setOrderId(order.getOrderId());
//                  orderDTO.setOrderDate(order.getOrderDate()); // add column in Order table
                    orderDTO.setOrderStatus(order.getOrderStatus());
                    orderDTO.setOrderTotalAmount(order.getOrderTotalAmount());
                    orderDTO.setUserId(order.getUserId());
                    return orderDTO;
                }).toList();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    public Order createOrder(Order order) {
        Order save = null;
        log.info("OrderController.createOrder");
        try {
            save = orderRepository.save(order);
            log.info("OrderController.createOrder: saveOrder: {}", save);
        } catch (Exception e) {
            log.error("OrderController.createOrder: Exception: {}", e.getStackTrace());
            throw new RuntimeException(e);
        }
        return save;
    }

    @Override
    public void updateOrder() {
        // TODO document why this method is empty
    }

    @Override
    public void cancelOrder(OrderDTO orderDTO) {
        try {
            log.info("OrderController.cancelOrder");
            orderRepository.cancelByOrderId(orderDTO.getOrderId());
            log.info("OrderController.cancelOrder: Order cancelled successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderItemsDTO> getOrderDetails(OrderDTO orderDTO) {
        List<OrderItemsDTO> orderItemsDTO = null;
        try {
            log.info("OrderController.getOrderDetails");
            orderItemsDTO = orderItemsRepository.findByOrderId(orderDTO.getOrderId());
            log.info("OrderController.getOrderDetails: Order details fetched successfully");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orderItemsDTO;
    }


    @Override
    public void getOrderByRestaurant() {
        // TODO document why this method is empty
    }

    @Override
    public void getOrderByUser() {
        // TODO document why this method is empty
    }

    @Override
    public void getOrderByStatus() {
        // TODO document why this method is empty
    }


}
