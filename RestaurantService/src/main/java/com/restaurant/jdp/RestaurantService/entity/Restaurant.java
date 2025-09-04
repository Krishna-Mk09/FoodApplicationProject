package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a Restaurant entity with complete business, address, and operational details.
 */
@Entity
@Table(name = "RESTAURANTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Column(name = "USER_ID")
    private Long userId;
    @Id
    @Column(name = "RESTAURANT_ID", nullable = false)
    private Long restaurantId;

    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "CONTACT_NUMBER", length = 15)
    private String contactNumber;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ADDRESS", length = 4000)
    private String address;

    @Column(name = "AREA", length = 100)
    private String area;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "STATE", length = 100)
    private String state;

    @Column(name = "PINCODE", length = 10)
    private String pincode;

    @Column(name = "CUISINE_TYPE", length = 200)
    private String cuisineType;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "TOTAL_RATINGS")
    private Integer totalRatings;

    @Column(name = "OPENING_TIME", length = 10)
    private String openingTime;

    @Column(name = "CLOSING_TIME", length = 10)
    private String closingTime;

    @Column(name = "IS_OPEN")
    private Boolean isOpen;

    @Column(name = "IS_VERIFIED")
    private Boolean isVerified;

    @Column(name = "IS_PURE_VEG")
    private Boolean isPureVeg;

    @Column(name = "ACCEPTS_ONLINE_ORDERS")
    private Boolean acceptsOnlineOrders;

    @Column(name = "ACCEPTS_TABLE_BOOKING")
    private Boolean acceptsTableBooking;

    @Column(name = "HOME_DELIVERY")
    private Boolean homeDelivery;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "DELIVERY_TIME_IN_MIN")
    private Integer deliveryTimeInMin;

    @Column(name = "DELIVERY_CHARGE")
    private Double deliveryCharge;

    @Column(name = "IMAGE_URL", length = 500)
    private String imageUrl;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "PAYMENT_METHODS", length = 100)
    private String paymentMethods;

    @Column(name = "ADDITIONAL_SERVICES", length = 100)
    private String additionalServices;

    @Column(name = "SOCIAL_MEDIA_LINKS", length = 500)
    private String socialMediaLinks;

    @Column(name = "WEBSITE_URL", length = 500)
    private String websiteUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "OWNER_ID")
    private RestaurantOwner owner;

    @ManyToOne
    @JoinColumn(name = "LICENCE_ID")
    private RestaurantLicence restaurantLicence;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menuList;

    //    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    //    private List<RestaurantReview> reviews;

}
