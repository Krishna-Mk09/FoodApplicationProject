package com.restaurant.jdp.RestaurantService.entity;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a promotional offer provided by a restaurant.
 */
@Entity
@Table(name = "restaurant_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Offer title
    private String description; // Offer description
    private Double discountPercent; // e.g., 20%
    private Double minOrderAmount; // Minimum order value
    private LocalDateTime validFrom; // Offer start date
    private LocalDateTime validTo; // Offer end date

    // Belongs to one restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
