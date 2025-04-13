package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the owner or manager of one or more restaurants.
 */
@Entity
@Table(name = "restaurant_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Full name
    private String email; // Login email
    private String phoneNumber; // For OTP login
    private String passwordHash; // Encrypted password
    private String profileImageUrl; // Optional profile image
    private LocalDateTime registeredAt; // Registration timestamp

    // One owner can own multiple restaurants
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;
}
