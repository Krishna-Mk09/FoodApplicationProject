package com.order.jdp.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 04-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantEmail;
    private String image;
    private Boolean isOpen;
    private String city;
    private String state;
    private String restaurantType;
    private String openingTime;
    private String closingTime;
    private Double latitude;
    private Double longitude;
    private String paymentMethods;
    private List<MenuItemEvent> menuItemEvent;
}
