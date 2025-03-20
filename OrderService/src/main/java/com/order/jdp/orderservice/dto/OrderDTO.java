package com.order.jdp.orderservice.dto;

import com.order.jdp.orderservice.entity.OrderItems;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
        private long orderId; //  pk
        private long userId;  // fk
        private long addressId; //fk
        private long noOfItems;
        private LocalDateTime orderBuyDate;
        private String orderStatus;
        private long orderDiscount;
        private long orderTax;
        private long orderTotalAmount;
        private long orderPaymentId; // fk
        private long orderDeliveryId; // fk
        private long orderRating;

        private List<OrderItems> orderItems;
}
