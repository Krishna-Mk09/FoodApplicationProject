package com.restaurant.jdp.RestaurantService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Author Name : M.V.Krishna
 * Date: 20-08-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemEvent {
    private Long itemId;
    private String itemName;
    private Double price;
    private Integer quantity;
    private Long restaurantId;
    private String description;
    private String category;
    private Boolean availability;
    private byte[] image;
    private String eventType;
    private String timestamp;
    private Boolean isOpen;
}
