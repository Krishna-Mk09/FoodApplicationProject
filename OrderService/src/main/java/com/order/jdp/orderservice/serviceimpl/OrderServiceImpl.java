package com.order.jdp.orderservice.serviceimpl;

import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.order.jdp.orderservice.dto.OrderDTO;
import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.entity.OrderItems;
import com.order.jdp.orderservice.repository.OrderItemsRepository;
import com.order.jdp.orderservice.repository.OrderRepository;
import com.order.jdp.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemsRepository orderItemsRepository;

    private final SequenceService sequenceService;

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

            if (order.getOrderId() == 0L) {
                long orderIdSeq = sequenceService.getSequenceByCustomer("ORDERS");
                if (!(orderIdSeq <= 0)) {
                    order.setOrderId(orderIdSeq);
                } else {
                    log.error("Sequence generation failed for  ORDERS  object");
                }
                for (OrderItems orderItems : order.getOrderItems()) {
                    orderItems.setOrderId(orderIdSeq);
                    long orderItemIdSeq = sequenceService.getSequenceByCustomer("ORDER_ITEMS");
                    if (orderItemIdSeq > 0) {
                        orderItems.setOrderItemId(orderItemIdSeq);
                    } else {
                        log.error("Sequence generation failed for  ORDER_ITEMS  object");
                    }
                }

            }
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


}


