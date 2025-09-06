package com.restaurant.jdp.RestaurantService.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.restaurant.jdp.RestaurantService.dto.MenuItemEvent;
import com.restaurant.jdp.RestaurantService.dto.RestaurantDto;
import com.restaurant.jdp.RestaurantService.entity.Menu;
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
    private final KafkaTemplate<String, RestaurantDto> kafkaTemplate;

    @Override
    @Transactional
    public String addRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        try {
            long restaurantsId =0;
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
            Restaurant byEmail = this.findByEmail(restaurant.getEmail());
            if (byEmail != null && byEmail.getEmail().equalsIgnoreCase(restaurant.getEmail()))
                return "Restaurant Already Exists";

            if (restaurant.getRestaurantId() == null || restaurant.getRestaurantId() == 0L) {
                 restaurantsId = sequenceService.getSequenceByCustomer("RESTAURANTS");
                restaurant.setRestaurantId(restaurantsId);
            }

            RestaurantOwner owner = RestaurantOwner.builder()
                    .ownerId(sequenceService.getSequenceByCustomer("RESTAURANT_OWNERS"))
                    .userId(currentUser.getUserId())
                    .email(currentUser.getSecondaryEmail())
                    .phoneNumber(currentUser.getPhoneNum())
                    .restaurantId(restaurant.getRestaurantId())
                    .build();
            ownerRepository.save(owner);
            restaurant.setOwner(owner);

            RestaurantLicence licence = RestaurantLicence.builder()
                    .licenceId(sequenceService.getSequenceByCustomer("RESTAURANT_LICENCES"))
                    .restaurantId(restaurant.getRestaurantId())
                    .licenceName(currentUser.getNameAsInLicense())
                    .nameAsInLicense(currentUser.getNameAsInLicense())
                    .licenceNumber(currentUser.getLicenseNumber())
                    .userId(currentUser.getUserId())
                    .build();
            licenceRepository.save(licence);
            restaurant.setRestaurantLicence(licence);

            if (restaurant.getMenuList() != null) {
                for (Menu item : restaurant.getMenuList()) {
                    if (item.getId() == null || item.getId() == 0L)
                        item.setId(sequenceService.getSequenceByCustomer("MENU"));
                    item.setRestaurant(restaurant);
                }
            }

            LocalDateTime now = LocalDateTime.now();
            restaurant.setUserId(currentUser.getUserId());
            restaurant.setCreatedAt(now);
            restaurant.setUpdatedAt(now);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDto restaurantDto = RestaurantDto.builder().restaurantId(savedRestaurant.getRestaurantId()).restaurantName(savedRestaurant.getName()).restaurantEmail(savedRestaurant.getEmail()).restaurantAddress(savedRestaurant.getAddress()).image(savedRestaurant.getImageUrl()).latitude(savedRestaurant.getLatitude()).longitude(savedRestaurant.getLongitude()).city(savedRestaurant.getCity()).state(savedRestaurant.getState()).restaurantType(savedRestaurant.getCuisineType()).closingTime(savedRestaurant.getClosingTime()).openingTime(savedRestaurant.getOpeningTime()).isOpen(savedRestaurant.getIsOpen()).paymentMethods(savedRestaurant.getPaymentMethods()).menuItemEvent(savedRestaurant.getMenuList() != null ? savedRestaurant.getMenuList().stream().map(item -> MenuItemEvent.builder().itemId(item.getId()).restaurantId(savedRestaurant.getRestaurantId()).itemName(item.getName()).description(item.getDescription()).price(item.getPrice()).quantity(item.getQuantity()).category(item.getCategory()).availability(item.getIsAvailable()).itemImage(item.getImageUrl()).timestamp(now.toString()).eventType("RestaurantCreated").build()).toList() : null).build();
            publishRestaurantAfterCommit(restaurantDto);

            return "Successfully Restaurant Saved!!!";
        } catch (Exception e) {
            log.error("Exception occurred while saving restaurant details: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishRestaurantAfterCommit(RestaurantDto dto) {
        kafkaTemplate.send(TOPIC, String.valueOf(dto.getRestaurantId()), dto);
        log.info("Published RestaurantDto AFTER commit: {}", dto);
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

}
