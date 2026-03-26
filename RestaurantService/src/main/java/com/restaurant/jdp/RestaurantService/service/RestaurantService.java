package com.restaurant.jdp.RestaurantService.service;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantService {
    String addRestaurant(Restaurant restaurant, String authHeade) throws Exception;

    String updateRestaurant(Restaurant restaurant, String authHeader) throws Exception;

    void deleteRestaurant(long id);

    Restaurant getRestaurantById(long id);

    Page<Restaurant> findAll(Pageable pageable);

    List<Restaurant> searchRestaurants(String query);

    Restaurant findByEmail(String email);

    Restaurant findRestaurantByUserId(long userId);


}
