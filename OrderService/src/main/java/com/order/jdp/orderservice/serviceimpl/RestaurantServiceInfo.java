package com.order.jdp.orderservice.serviceimpl;


import com.order.jdp.orderservice.dto.MenuItemEvent;
import com.order.jdp.orderservice.dto.RestaurantDto;
import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.RestaurantInfo;
import com.order.jdp.orderservice.repository.ItemsInfoRepository;
import com.order.jdp.orderservice.repository.RestaurantRepository;
import com.order.jdp.orderservice.service.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/*
 * Author Name : M.V.Krishna
 * Date: 06-09-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceInfo implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ItemsInfoRepository itemsInfoRepository;

    @KafkaListener(topics = "menu-items", groupId = "order-service")
    @Transactional
    public void consumeRestaurantEvent(RestaurantDto dto) {
        log.info("Received Restaurant Event: {}", dto);

        RestaurantInfo restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElse(RestaurantInfo.builder().restaurantId(dto.getRestaurantId()).build());

        restaurant.setRestaurantName(dto.getRestaurantName());
        restaurant.setRestaurantAddress(dto.getRestaurantAddress());
        restaurant.setRestaurantEmail(dto.getRestaurantEmail());
        restaurant.setCity(dto.getCity());
        restaurant.setState(dto.getState());
        restaurant.setRestaurantType(dto.getRestaurantType());
        restaurant.setOpeningTime(dto.getOpeningTime());
        restaurant.setClosingTime(dto.getClosingTime());
        restaurant.setIsOpen(dto.getIsOpen());
        restaurant.setPaymentMethods(dto.getPaymentMethods());
        restaurant.setLatitude(dto.getLatitude());
        restaurant.setLongitude(dto.getLongitude());
        restaurantRepository.save(restaurant);

        if (dto.getMenuItemEvent() != null) {
            for (MenuItemEvent item : dto.getMenuItemEvent()) {
                switch (item.getEventType()) {
                    case "Created" -> {
                        if (!itemsInfoRepository.existsById(item.getItemId())) {
                            ItemsInfo newItem = ItemsInfo.builder().itemId(item.getItemId()).restaurant(restaurant).itemName(item.getItemName()).price(item.getPrice()).quantity(item.getQuantity()).description(item.getDescription()).category(item.getCategory()).availability(item.getAvailability()).itemImage(item.getItemImage()).eventType(item.getEventType()).timestamp(item.getTimestamp()).build();
                            itemsInfoRepository.save(newItem);
                            log.info(" Created menu item: {}", newItem.getItemName());
                        }
                    }
                    case "Updated" -> {
                        itemsInfoRepository.findById(item.getItemId()).ifPresent(existingItem -> {
                            if (item.getItemName() != null) existingItem.setItemName(item.getItemName());
                            if (item.getPrice() != null) existingItem.setPrice(item.getPrice());
                            if (item.getQuantity() != null) existingItem.setQuantity(item.getQuantity());
                            if (item.getDescription() != null) existingItem.setDescription(item.getDescription());
                            if (item.getCategory() != null) existingItem.setCategory(item.getCategory());
                            if (item.getAvailability() != null) existingItem.setAvailability(item.getAvailability());
                            if (item.getItemImage() != null) existingItem.setItemImage(item.getItemImage());
                            existingItem.setEventType(item.getEventType());
                            existingItem.setTimestamp(item.getTimestamp());
                            itemsInfoRepository.save(existingItem);
                            log.info(" Updated menu item: {}", existingItem.getItemName());
                        });
                    }
                    case "Deleted" -> {
                        itemsInfoRepository.deleteById(item.getItemId());
                        log.info(" Deleted menu item with ID: {}", item.getItemId());
                    }
                    default -> log.warn(" Unknown event type: {}", item.getEventType());
                }
            }
        }
    }


    @Override
    public RestaurantInfo saveRestaurantDetails(RestaurantInfo restaurant) {
        return null;
    }
}
