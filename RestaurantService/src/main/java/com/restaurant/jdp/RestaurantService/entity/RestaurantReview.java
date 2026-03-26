package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

/**
 * Represents a customer review and rating for a restaurant.
 */
@Entity
@Table(name = "RESTAURENT_REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
