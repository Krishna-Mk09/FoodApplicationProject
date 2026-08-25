package com.order.jdp.orderservice.repository;

import com.order.jdp.orderservice.entity.ItemsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemsInfoRepository extends JpaRepository<ItemsInfo, Long> {

    List<ItemsInfo> findByRestaurantId(Long restaurantId);

}
