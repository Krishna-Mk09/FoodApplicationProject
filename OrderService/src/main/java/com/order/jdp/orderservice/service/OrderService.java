package com.order.jdp.orderservice.service;

import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.Order;

import java.util.List;


public interface OrderService {
    Order createOrder(Order order);

    void updateOrder();

    void cancelOrder(OrderDTO orderDTO);

    List<OrderItemsDTO> getOrderDetails(OrderDTO orderDTO);

    List<OrderDTO> getAllOrders(long userId);

}
