package com.order.jdp.orderservice.dto;

import com.order.jdp.orderservice.entity.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private long orderId;
    private long userId;
    private long addressId;
    private long noOfItems;
    private LocalDateTime orderBuyDate;
    private String orderStatus;
    private long orderDiscount;
    private long orderTax;
    private long orderTotalAmount;
    private long orderPaymentId;
    private long orderDeliveryId;
    private long orderRating;
    private List<OrderItems> orderItems;
}
