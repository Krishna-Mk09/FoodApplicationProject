package com.restaurant.jdp.RestaurantService.repository;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByEmail(String email);
    Page<Restaurant> findAll(Pageable pageable);

    Restaurant findByUserId(Long userId);
}
