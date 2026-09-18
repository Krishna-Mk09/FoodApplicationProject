package com.order.jdp.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CART_ITEMS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {

    @Id
    @Column(name = "CART_ITEM_ID")
    private long cartItemId;

    @Column(name = "RESTAURANT_ITEM_ID")
    private long restaurantItemId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "ITEM_PRICE")
    private double itemPrice;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

    @Column(name = "CART_ID")
    private long cartId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CART_ID", referencedColumnName = "CART_ID", insertable = false, updatable = false)
    private Cart cart;
}
