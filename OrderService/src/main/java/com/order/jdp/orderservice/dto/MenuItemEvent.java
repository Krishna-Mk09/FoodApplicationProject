package com.order.jdp.orderservice.dto;


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
    private Long restaurantId;
    private String itemName;
    private Double price;
    private Integer quantity;
    private String description;
    private String category;
    private Boolean availability;
    private String itemImage;
    private String eventType;
    private String timestamp;
}
