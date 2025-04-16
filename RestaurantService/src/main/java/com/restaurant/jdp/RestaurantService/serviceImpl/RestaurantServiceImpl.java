package com.restaurant.jdp.RestaurantService.serviceImpl;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import com.restaurant.jdp.RestaurantService.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {
    // Implement the methods defined in the RestaurantService interface
    @Override
    public void addRestaurant(Restaurant restaurant) {
        // Implementation for adding a restaurant
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        // Implementation for updating a restaurant
    }

    @Override
    public void deleteRestaurant(long id) {
        // Implementation for deleting a restaurant
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        // Implementation for getting a restaurant by ID
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        // Implementation for getting all restaurants
        return null;
    }

    @Override
    public List<Restaurant> searchRestaurants(String query) {
        // Implementation for searching restaurants based on a query
        return null;
    }

}
