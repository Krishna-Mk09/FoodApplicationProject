package com.restaurant.jdp.RestaurantService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "USER_NAME")
    private long userId;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 15)
    private String phoneNumber;

    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Restaurant restaurant;


}
