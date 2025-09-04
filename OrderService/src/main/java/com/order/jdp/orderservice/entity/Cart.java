package com.order.jdp.orderservice.entity;


import jakarta.persistence.*;

import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 18-08-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Entity
@Table(name = "CART")
public class Cart {
    @Id
    @Column(name = "CART_ID", nullable = false)
    private Long cartId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "RESTAURANT_ID", nullable = false)
    private Long restaurantId;

    @OneToMany(mappedBy = "Cart", cascade = CascadeType.ALL)
    List<CartItems> cartItemsList;
}
