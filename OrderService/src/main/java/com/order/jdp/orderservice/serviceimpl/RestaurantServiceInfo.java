package com.order.jdp.orderservice.serviceimpl;


import com.order.jdp.orderservice.dto.RestaurantDto;
import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.RestaurantInfo;
import com.order.jdp.orderservice.repository.ItemsInfoRepository;
import com.order.jdp.orderservice.repository.RestaurantRepository;
import com.order.jdp.orderservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 06-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceInfo implements RestaurantService {
    private  final RestaurantRepository restaurantRepository;
    private  final ItemsInfoRepository itemsInfoRepository;

    @KafkaListener(topics = "menu-items", groupId = "order-service")
    public void consumeRestaurantEvent(RestaurantDto dto) {
        RestaurantInfo restaurant = RestaurantInfo.builder()
                .restaurantId(dto.getRestaurantId())
                .restaurantName(dto.getRestaurantName())
                .restaurantAddress(dto.getRestaurantAddress())
                .restaurantEmail(dto.getRestaurantEmail())
                .city(dto.getCity())
                .state(dto.getState())
                .restaurantType(dto.getRestaurantType())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .isOpen(dto.getIsOpen())
                .paymentMethods(dto.getPaymentMethods())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        restaurantRepository.save(restaurant);

        if (dto.getMenuItemEvent() != null) {
            List<ItemsInfo> items = dto.getMenuItemEvent().stream()
                    .map(item -> ItemsInfo.builder()
                            .itemId(item.getItemId())
                            .restaurant(restaurant)
                            .itemName(item.getItemName())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .description(item.getDescription())
                            .category(item.getCategory())
                            .availability(item.getAvailability())
                            .itemImage(item.getItemImage())
                            .eventType(item.getEventType())
                            .timestamp(item.getTimestamp())
                            .build())
                    .toList();
            itemsInfoRepository.saveAll(items);
        }
    }

    @Override
    public RestaurantInfo saveRestaurantDetails(RestaurantInfo restaurant) {
        return null;
    }
}
