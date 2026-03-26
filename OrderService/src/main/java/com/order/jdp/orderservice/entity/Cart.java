package com.order.jdp.orderservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Author Name : M.V.Krishna
 * Date: 18-08-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
