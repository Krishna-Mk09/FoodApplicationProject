package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a menu item listed by a restaurant.
 */
@Entity
@Table(name = "MENU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    private Long id;
    private String name; // Name of the dish
    private String description; // Dish details
    private Double price; // Current price
    private String category; // e.g., "Starter", "Main Course"
    private Boolean isVeg; // True if vegetarian
    private Boolean isAvailable; // True if available to order
    private byte[] imageUrl; // Dish image
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    private Restaurant restaurant;
}
