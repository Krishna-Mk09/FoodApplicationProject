package com.order.jdp.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Data
@Entity
@Table(name = "`order_items`")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private long orderItemId;
    @Column(name = "order_item_name")
    private String orderItemName;
    @Column(name = "restaurant_id")
    private long restaurantId;
    @Column(name = "restaurant_item_id")
    private long restaurantItemId;
    @Column(name = "order_item_quantity")
    private long orderItemQuantity;
    @Column(name = "order_item_price")
    private long orderItemPrice;
    @Column(name = "order_item_discount")
    private long orderItemDiscount;
    @Column(name = "order_item_tax")
    private long orderItemTax;
    @Column(name = "order_item_total_amount")
    private long orderItemTotalAmount;
    @Column(name = "order_item_status")
    private String orderItemStatus;
    @Column(name = "order_item_rating")
    private long orderItemRating;
    @Column(name = "order_id")
    private long orderId;

}
