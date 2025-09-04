package com.order.jdp.orderservice.entity;


import jakarta.persistence.*;

/*
 * Author Name : M.V.Krishna
 * Date: 19-08-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Entity
@Table(name = "CART_ITEMS")
public class CartItems {

    @Id
    private Long cartItemId;

    @ManyToOne
    private Cart cart;

    @Column(name = "ITEM_ID", nullable = false)
    private Long itemId;

    @Column(name = "ITEM_NAME", nullable = false, length = 1000)
    private String itemName;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PRICE", nullable = false)
    private Double price;

}
