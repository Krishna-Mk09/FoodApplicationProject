package com.order.jdp.orderservice.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.RestaurantInfo;
import com.order.jdp.orderservice.repository.ItemsInfoRepository;
import com.order.jdp.orderservice.repository.RestaurantRepository;
import com.order.jdp.orderservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ItemsInfoRepository itemsInfoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = {"foodapp.food_application_platform.restaurants", "foodapp.food_application_platform.menu"}, groupId = "orderservice")
    public void consumeDebeziumEvent(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            JsonNode root = objectMapper.readTree(message);
            JsonNode payload = root.get("payload");

            if (payload != null) {
                String op = payload.get("op").asText();
                JsonNode after = payload.get("after");
                JsonNode before = payload.get("before");

                switch (topic) {
                    case "foodapp.food_application_platform.restaurants":
                        switch (op) {
                            case "c", "u":
                                if (after != null && !after.isNull()) {
                                    RestaurantInfo restaurant = objectMapper.treeToValue(after, RestaurantInfo.class);
                                    restaurantRepository.save(restaurant);
                                    System.out.println("Saved/Updated Restaurant: " + restaurant);
                                }
                                break;
                            case "d":
                                if (before != null && !before.isNull()) {
                                    Long id = before.get("restaurant_id").asLong();
                                    restaurantRepository.deleteById(id);
                                    System.out.println("Deleted Restaurant ID: " + id);
                                }
                                break;
                        }
                        break;

                    case "foodapp.food_application_platform.menu":
                        switch (op) {
                            case "c", "u":
                                if (after != null && !after.isNull()) {
                                    ItemsInfo item = objectMapper.treeToValue(after, ItemsInfo.class);
                                    itemsInfoRepository.save(item);
                                    System.out.println("Saved/Updated Item: " + item);
                                }
                                break;
                            case "d":
                                if (before != null && !before.isNull()) {
                                    Long id = before.get("item_id").asLong();
                                    itemsInfoRepository.deleteById(id);
                                    System.out.println("Deleted Item ID: " + id);
                                }
                                break;
                        }
                        break;
                }
            }

        } catch (Exception e) {
            log.error("Exception occurred while consuming the data: {}", e.getMessage(), e);
        }
    }

}
