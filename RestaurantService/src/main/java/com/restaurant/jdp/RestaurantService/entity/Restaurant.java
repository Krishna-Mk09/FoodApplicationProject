package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a Restaurant entity with complete business, address, and operational details.
 */
@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    private String name; // Restaurant name
    private String description; // About the restaurant
    private String contactNumber; // Business phone number
    private String email; // Business email ID

    private String address; // Full address
    private String area; // Locality / landmark
    private String city; // City
    private String state; // State
    private String pincode; // Postal code

    private String cuisineType; // e.g., "Chinese, Indian"
    private Double averageCostForTwo; // INR value

    private Double rating; // Average customer rating
    private Integer totalRatings; // Total number of reviews

    private String openingTime; // e.g., "09:00 AM"
    private String closingTime; // e.g., "11:00 PM"
    private Boolean isOpen; // True if open now

    private Boolean isVerified; // Verified by Zomato/admin
    private Boolean isPureVeg; // True if pure vegetarian
    private Boolean acceptsOnlineOrders; // Accepts online orders
    private Boolean acceptsTableBooking; // Accepts dine-in bookings
    private Boolean homeDelivery; // Offers delivery

    private Double latitude;
    private Double longitude;


    private Integer deliveryTimeInMin; // Estimated delivery time
    private Double deliveryCharge; // Delivery fee

    private String imageUrl; // Banner or restaurant image

    private LocalDateTime createdAt; // Created timestamp
    private LocalDateTime updatedAt; // Last modified

    // Many restaurants can belong to one owner
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private RestaurantOwner owner;

    // One restaurant has many menu items
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    // One restaurant can have many customer reviews
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantReview> reviews;

    // Offers active for this restaurant

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantOffer> offers;
}
