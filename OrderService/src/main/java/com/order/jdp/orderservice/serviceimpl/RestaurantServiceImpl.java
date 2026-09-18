package com.order.jdp.orderservice.serviceimpl;


import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.RestaurantInfo;
import com.order.jdp.orderservice.repository.ItemsInfoRepository;
import com.order.jdp.orderservice.repository.RestaurantRepository;
import com.order.jdp.orderservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ItemsInfoRepository itemsInfoRepository;


    @Override
    public List<RestaurantInfo> getAllRestaurants() {
        log.info("Fetching all restaurants from repository");
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<RestaurantInfo> getRestaurantById(Long id) {
        log.info("Fetching restaurant by ID: {}", id);
        return restaurantRepository.findById(id);
    }

    @Override
    public List<ItemsInfo> getRestaurantMenu(Long restaurantId) {
        log.info("Fetching menu for restaurant ID: {}", restaurantId);
        return itemsInfoRepository.findByRestaurantId(restaurantId);
    }


}
