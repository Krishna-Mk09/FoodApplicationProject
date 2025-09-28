package com.order.jdp.orderservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Author Name : M.V.Krishna
 * Date: 06-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ItemsInfo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "image_url")
    private String ImageUrl;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "isVeg")
    private Boolean isVeg;

    @Column(name = "isAvailable")
    private Boolean isAvailable;

}
