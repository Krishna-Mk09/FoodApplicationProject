package com.restaurant.jdp.RestaurantService.service;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantService {
    String addRestaurant(Restaurant restaurant, String authHeade) throws Exception;
    void updateRestaurant(Restaurant restaurant, String authHeader) throws Exception;
    void deleteRestaurant(long id);
    Restaurant getRestaurantById(long id);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> searchRestaurants(String query);












//    List<Restaurant> searchRestaurants(String query, String cuisineType);
//    List<Restaurant> searchRestaurants(String query, String cuisineType, double rating);
//    List<Restaurant> searchRestaurants(String query, String cuisineType, double rating, double averageCostForTwo);
}
