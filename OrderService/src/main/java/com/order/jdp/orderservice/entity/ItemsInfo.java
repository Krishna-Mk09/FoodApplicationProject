package com.order.jdp.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MENU")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsInfo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "RESTAURANT_ID")
    @JsonProperty("restaurantId")
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
    @JsonProperty("imageUrl")
    private String imageUrl;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "is_veg")
    @JsonProperty("isVeg")
    private Boolean isVeg;

    @Column(name = "is_available")
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @JsonProperty("image")
    public String getImage() {
        return imageUrl;
    }

    @JsonProperty("rating")
    public Double getRating() {
        return 4.5;
    }

    @JsonProperty("votes")
    public Integer getVotes() {
        return 20;
    }
}
