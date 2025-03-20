package com.order.jdp.orderservice.service;

import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.Order;


import java.util.List;


public interface OrderService {
    public Order createOrder(Order order);
    public void updateOrder();
    public void cancelOrder(OrderDTO orderDTO);
    public List<OrderItemsDTO> getOrderDetails(OrderDTO orderDTO);
    public void getOrderByRestaurant();
    public void getOrderByUser();
    public void getOrderByStatus();

    List<OrderDTO> getAllOrders(long userId);

}
