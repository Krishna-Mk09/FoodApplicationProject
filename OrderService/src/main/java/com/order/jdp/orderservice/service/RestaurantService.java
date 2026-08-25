package com.order.jdp.orderservice.service;

import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.RestaurantInfo;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    List<RestaurantInfo> getAllRestaurants();

    Optional<RestaurantInfo> getRestaurantById(Long id);

    List<ItemsInfo> getRestaurantMenu(Long restaurantId);

}
