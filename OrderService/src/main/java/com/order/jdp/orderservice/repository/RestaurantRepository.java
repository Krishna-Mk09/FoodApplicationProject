package com.order.jdp.orderservice.repository;


import com.order.jdp.orderservice.entity.RestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author Name : M.V.Krishna
 * Date: 06-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
public interface RestaurantRepository extends JpaRepository<RestaurantInfo, Long> {

}
