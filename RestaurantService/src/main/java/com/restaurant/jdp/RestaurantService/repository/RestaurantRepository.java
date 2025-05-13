package com.restaurant.jdp.RestaurantService.repository;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

//    List<Restaurant> findByNameContainingIgnoreCase(String name);
//    List<Restaurant> findByAreaContainingIgnoreCase(String area);
//    List<Restaurant> findByCityContainingIgnoreCase(String city);
//    List<Restaurant> findByCuisineTypeContainingIgnoreCase(String cuisineType);
}
