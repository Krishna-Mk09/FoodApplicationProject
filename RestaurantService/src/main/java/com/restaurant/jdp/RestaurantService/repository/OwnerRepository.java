package com.restaurant.jdp.RestaurantService.repository;

import com.restaurant.jdp.RestaurantService.entity.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Author Name : M.V.Krishna
 * Date: 11-05-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Repository
public interface OwnerRepository extends JpaRepository<RestaurantOwner, Long> {

}
