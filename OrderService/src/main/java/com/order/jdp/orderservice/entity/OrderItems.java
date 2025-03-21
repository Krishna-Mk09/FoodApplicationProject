package com.order.jdp.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ORDER_ITEMS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItems {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    private long orderItemId;

    @Column(name = "ORDER_ITEM_NAME")
    private String orderItemName;

    @Column(name = "RESTAURANT_ID")
    private long restaurantId;

    @Column(name = "RESTAURANT_ITEM_ID")
    private long restaurantItemId;

    @Column(name = "ORDER_ITEM_QUANTITY")
    private long orderItemQuantity;

    @Column(name = "ORDER_ITEM_PRICE")
    private long orderItemPrice;

    @Column(name = "ORDER_ITEM_DISCOUNT")
    private long orderItemDiscount;

    @Column(name = "ORDER_ITEM_TAX")
    private long orderItemTax;

    @Column(name = "ORDER_ITEM_TOTAL_AMOUNT")
    private long orderItemTotalAmount;

    @Column(name = "ORDER_ITEM_STATUS")
    private String orderItemStatus;

    @Column(name = "ORDER_ITEM_RATING")
    private long orderItemRating;


    @Column(name = "ORDER_ID") // Prevent duplicate mapping
    private long orderId;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID", insertable = false, updatable = false)
    private Order order;
}
