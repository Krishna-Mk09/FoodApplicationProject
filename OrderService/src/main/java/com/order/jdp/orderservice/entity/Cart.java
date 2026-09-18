package com.order.jdp.orderservice.entity;


import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
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

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount = 0.0;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
