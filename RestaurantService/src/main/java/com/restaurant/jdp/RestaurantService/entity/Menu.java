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
    private String name;   // Name of the dish
    private String description;   // Dish details
    private Double price;  // Current price
    private String category; // "Starter", "Main Course"
    private Boolean isVeg; //  vegetarian
    private Boolean isAvailable;  // available to order
    private String imageUrl;  // Dish image
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    private Restaurant restaurant;

}
