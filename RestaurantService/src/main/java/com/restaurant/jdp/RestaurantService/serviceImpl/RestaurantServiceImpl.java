package com.restaurant.jdp.RestaurantService.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.restaurant.jdp.RestaurantService.dto.MenuItemEvent;
import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import com.restaurant.jdp.RestaurantService.entity.RestaurantLicence;
import com.restaurant.jdp.RestaurantService.entity.RestaurantOwner;
import com.restaurant.jdp.RestaurantService.repository.LicenceRepository;
import com.restaurant.jdp.RestaurantService.repository.OwnerRepository;
import com.restaurant.jdp.RestaurantService.repository.RestaurantRepository;
import com.restaurant.jdp.RestaurantService.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private static final String TOPIC = "menu-items";
    private final RestaurantRepository restaurantRepository;
    private final LicenceRepository licenceRepository;
    private final OwnerRepository ownerRepository;
    private final SequenceService sequenceService;
    private final KafkaTemplate<String, MenuItemEvent> kafkaTemplate;

    @Override
    @Transactional
    public String addRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        try {
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
            Restaurant byEmail = this.findByEmail(restaurant.getEmail());
            if (byEmail != null && byEmail.getEmail().equalsIgnoreCase(restaurant.getEmail()))
                return "Restaurant Already Exists";
            if (restaurant.getRestaurantLicence() == null) restaurant.setRestaurantLicence(new RestaurantLicence());

            if (restaurant.getRestaurantId() == null || restaurant.getRestaurantId() == 0L) {
                long restaurantId = sequenceService.getSequenceByCustomer("RESTAURANTS");
                restaurant.setRestaurantId(restaurantId);
            }
            RestaurantOwner restaurantOwner = new RestaurantOwner();
            restaurantOwner.setOwnerId(sequenceService.getSequenceByCustomer("RESTAURANT_OWNERS"));
            restaurantOwner.setUserId(currentUser.getUserId());
            restaurantOwner.setRestaurantId(restaurant.getRestaurantId());
            restaurantOwner.setEmail(currentUser.getSecondaryEmail());
            restaurantOwner.setPhoneNumber(currentUser.getPhoneNum());
            ownerRepository.save(restaurantOwner);
            restaurant.setOwner(restaurantOwner);
            RestaurantLicence licence = restaurant.getRestaurantLicence();
            if (licence.getLicenceId() == null || licence.getLicenceId() == 0L)
                licence.setLicenceId(sequenceService.getSequenceByCustomer("RESTAURANT_LICENCES"));

            licence.setRestaurantId(restaurant.getRestaurantId());
            licence.setLicenceName(currentUser.getNameAsInLicense());
            licence.setLicenceNumber(currentUser.getLicenseNumber());
            licence.setNameAsInLicense(currentUser.getNameAsInLicense());
            licence.setUserId(currentUser.getUserId());
            licenceRepository.save(licence);
            restaurant.setRestaurantLicence(licence);
            List<MenuItemEvent> events = null;
            if (restaurant.getMenuList() != null) {
                events = restaurant.getMenuList().stream().map(item -> {
                    try {
                        item.setRestaurant(restaurant);
                        item.setId(sequenceService.getSequenceByCustomer("MENU"));
                    } catch (Exception e) {
                        log.error("Exception occurred while getting sequence value: {}", ExceptionUtils.getStackTrace(e));
                        throw new IllegalArgumentException("Error while generating Menu ID");
                    }
                    return MenuItemEvent.builder().itemId(item.getId())
                            .restaurantId(restaurant.getRestaurantId()).description(item.getDescription())
                            .isOpen(restaurant.getIsOpen()).availability(item.getIsAvailable())
                            .category(item.getCategory()).image(item.getImageUrl()).eventType("RestaurantCreated")
                            .itemName(item.getName()).price(item.getPrice()).quantity(item.getQuantity()).build();
                }).toList();
            }
            LocalDateTime now = LocalDateTime.now();
            restaurant.setUserId(currentUser.getUserId());
            restaurant.setCreatedAt(now);
            restaurant.setUpdatedAt(now);
            restaurantRepository.save(restaurant);

            if (events != null && !events.isEmpty()) {
                publishMenuEventsAfterCommit(events);
            }

            return "Successfully Restaurant Saved!!!";
        } catch (Exception e) {
            log.error("Exception occurred while saving restaurant details: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


    @Override
    @Transactional
    public void updateRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        try {
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
            if (currentUser == null || !currentUser.getRole().equals("OWNER")) {
                throw new IllegalArgumentException("Only restaurant owners can update restaurant details");
            }

            Restaurant existingRestaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurant.getRestaurantId()));
            existingRestaurant.setName(restaurant.getName());
            existingRestaurant.setDescription(restaurant.getDescription());
            existingRestaurant.setContactNumber(restaurant.getContactNumber());
            existingRestaurant.setAddress(restaurant.getAddress());
            existingRestaurant.setArea(restaurant.getArea());
            existingRestaurant.setCity(restaurant.getCity());
            existingRestaurant.setState(restaurant.getState());
            existingRestaurant.setPincode(restaurant.getPincode());
            existingRestaurant.setCuisineType(restaurant.getCuisineType());
            existingRestaurant.setOpeningTime(restaurant.getOpeningTime());
            existingRestaurant.setClosingTime(restaurant.getClosingTime());
            existingRestaurant.setIsOpen(restaurant.getIsOpen());
            existingRestaurant.setIsPureVeg(restaurant.getIsPureVeg());
            existingRestaurant.setAcceptsOnlineOrders(restaurant.getAcceptsOnlineOrders());
            existingRestaurant.setAcceptsTableBooking(restaurant.getAcceptsTableBooking());
            existingRestaurant.setHomeDelivery(restaurant.getHomeDelivery());
            existingRestaurant.setLatitude(restaurant.getLatitude());
            existingRestaurant.setLongitude(restaurant.getLongitude());
            existingRestaurant.setDeliveryTimeInMin(restaurant.getDeliveryTimeInMin());
            existingRestaurant.setDeliveryCharge(restaurant.getDeliveryCharge());
            existingRestaurant.setImageUrl(restaurant.getImageUrl());
            existingRestaurant.setPaymentMethods(restaurant.getPaymentMethods());
            existingRestaurant.setAdditionalServices(restaurant.getAdditionalServices());
            existingRestaurant.setSocialMediaLinks(restaurant.getSocialMediaLinks());
            existingRestaurant.setWebsiteUrl(restaurant.getWebsiteUrl());
            existingRestaurant.setUpdatedAt(LocalDateTime.now());
            restaurantRepository.save(existingRestaurant);
        } catch (Exception e) {
            log.error("Exception occurred while updating restaurant details: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void deleteRestaurant(long id) {
        try {
            Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + id));
            if (restaurant.getOwner() != null) {
                ownerRepository.deleteById(restaurant.getOwner().getOwnerId());
            }
            if (restaurant.getRestaurantLicence() != null) {
                licenceRepository.deleteById(restaurant.getRestaurantLicence().getLicenceId());
            }
            restaurantRepository.deleteById(id);
        } catch (Exception e) {
            log.error(" Exception occurred while deleting restaurant details {} ", ExceptionUtils.getStackTrace(e));
            throw new IllegalArgumentException("Restaurant not found with ID: " + id);
        }
    }


    @Override
    public Restaurant findByEmail(String email) {
        return restaurantRepository.findByEmail(email);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return null;
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);

    }

    public Page<Restaurant> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }


    @Override
    public List<Restaurant> searchRestaurants(String query) {
        return null;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishMenuEventsAfterCommit(List<MenuItemEvent> events) {
        events.forEach(event -> {
            kafkaTemplate.send(TOPIC, String.valueOf(event.getItemId()), event);
            log.info(" Published MenuItemEvent AFTER commit: {}", event);
        });
    }

}
