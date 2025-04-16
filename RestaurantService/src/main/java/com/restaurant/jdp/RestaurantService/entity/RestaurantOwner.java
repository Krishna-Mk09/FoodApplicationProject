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
    @Column(name = "owner_id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // Full name

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email; // Login email

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber; // For OTP login

    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash; // Encrypted password

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl; // Optional profile image

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt; // Registration timestamp

    // One owner can own multiple restaurants
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    @Column(name = "restaurant_id", nullable = false)
    private long restaurantId; // ID of the restaurant owned by this owner
}