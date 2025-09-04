//package com.restaurant.jdp.RestaurantService.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.security.core.userdetails.User;
//
//import java.time.LocalDateTime;
//
///**
// * Represents a customer review and rating for a restaurant.
// */
//@Entity
//@Table(name = "RESTAURENT_REVIEWS")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RestaurantReview {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Double rating; // Rating value
//    private String comment; // Review text
//    private LocalDateTime createdAt; // When the review was made
//
//    // Belongs to a specific restaurant
//    @ManyToOne
//    @JoinColumn(name = "restaurant_id")
//    private Restaurant restaurant;
//
//    // Given by a specific user
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//}
