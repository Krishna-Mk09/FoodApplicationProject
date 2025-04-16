package com.restaurant.jdp.RestaurantService.service;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RestaurantService {
    // Define methods that will be implemented by the service class
    void addRestaurant(Restaurant restaurant);
    void updateRestaurant(Restaurant restaurant);
    void deleteRestaurant(long id);
    Restaurant getRestaurantById(long id);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> searchRestaurants(String query);
//    List<Restaurant> searchRestaurants(String query, String cuisineType);
//    List<Restaurant> searchRestaurants(String query, String cuisineType, double rating);
//    List<Restaurant> searchRestaurants(String query, String cuisineType, double rating, double averageCostForTwo);
}
