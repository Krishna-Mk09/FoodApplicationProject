package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a menu item listed by a restaurant.
 */
@Entity
@Table(name = "ITEMS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Items {
    @Id
    private Long id;
    private String name; // Name of the dish
    private String description; // Dish details
    private Double price; // Current price
    private String category; // e.g., "Starter", "Main Course"
    private Boolean isVeg; // True if vegetarian
    private Boolean isAvailable; // True if available to order
    private String imageUrl; // Dish image
    private LocalDateTime createdAt; // Timestamp
    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    private Restaurant restaurant;
}
