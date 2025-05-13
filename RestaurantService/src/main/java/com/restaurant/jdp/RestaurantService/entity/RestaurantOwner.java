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
    @Column(name = "OWNER_ID", nullable = false)
    private long ownerId;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 500)
    private String passwordHash;
    @Column(name = "PROFILE_IMAGE_URL", length = 500)
    private String profileImageUrl;

    @Column(name = "REGISTERED_AT", nullable = false)
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    @Column(name = "RESTAURANT_ID", nullable = false)
    private Long restaurantId;
}
