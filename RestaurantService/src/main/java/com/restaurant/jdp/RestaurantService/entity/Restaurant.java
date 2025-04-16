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
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 4000)
    private String address;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "cuisine_type", length = 200)
    private String cuisineType;

    @Column(name = "average_cost_for_two")
    private Double averageCostForTwo;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "total_ratings")
    private Integer totalRatings;

    @Column(name = "opening_time", length = 10)
    private String openingTime;

    @Column(name = "closing_time", length = 10)
    private String closingTime;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "is_pure_veg")
    private Boolean isPureVeg;

    @Column(name = "accepts_online_orders")
    private Boolean acceptsOnlineOrders;

    @Column(name = "accepts_table_booking")
    private Boolean acceptsTableBooking;

    @Column(name = "home_delivery")
    private Boolean homeDelivery;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "delivery_time_in_min")
    private Integer deliveryTimeInMin;

    @Column(name = "delivery_charge")
    private Double deliveryCharge;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private RestaurantOwner owner;

    @ManyToOne
    @JoinColumn(name = "licence_id")
    private RestaurantLicence restaurantLicence;

//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<MenuItem> menuItems;

//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<RestaurantReview> reviews;

//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private List<RestaurantOffer> offers;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "payment_methods", length = 100)
    private String paymentMethods;

    @Column(name = "additional_services", length = 100)
    private String additionalServices;

    @Column(name = "social_media_links", length = 500)
    private String socialMediaLinks;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;
}