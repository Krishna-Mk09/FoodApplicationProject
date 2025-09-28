package com.order.jdp.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDTO {

    private long orderItemId;
    private String orderItemName;
    private long restaurantId;
    private long restaurantItemId;
    private long orderItemQuantity;
    private long orderItemPrice;
    private long orderItemDiscount;
    private long orderItemTax;
    private long orderItemTotalAmount;
    private String orderItemStatus;
    private long orderItemRating;
    private long orderId;

}
