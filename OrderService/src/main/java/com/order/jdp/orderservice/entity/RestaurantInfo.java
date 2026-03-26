package com.order.jdp.orderservice.entity;


/*
 * Author Name : M.V.Krishna
 * Date: 06-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantInfo {

    @Id
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "image")
    private String image;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "restaurant_type")
    private String cuisineType;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "payment_methods")
    private String paymentMethods;

}


