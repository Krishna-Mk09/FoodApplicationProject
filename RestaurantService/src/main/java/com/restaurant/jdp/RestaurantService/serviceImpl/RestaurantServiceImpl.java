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

import static java.time.LocalTime.now;


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

    public static RestaurantDto manageRestaurantDto(Restaurant savedRestaurant,String eventType) {
        return RestaurantDto.builder().restaurantId(savedRestaurant.getRestaurantId()).restaurantName(savedRestaurant.getName()).restaurantEmail(savedRestaurant.getEmail()).restaurantAddress(savedRestaurant.getAddress()).image(savedRestaurant.getImageUrl()).latitude(savedRestaurant.getLatitude()).longitude(savedRestaurant.getLongitude()).city(savedRestaurant.getCity()).state(savedRestaurant.getState()).restaurantType(savedRestaurant.getCuisineType()).closingTime(savedRestaurant.getClosingTime()).openingTime(savedRestaurant.getOpeningTime()).isOpen(savedRestaurant.getIsOpen()).paymentMethods(savedRestaurant.getPaymentMethods()).menuItemEvent(savedRestaurant.getMenuList() != null ? savedRestaurant.getMenuList().stream().map(item -> MenuItemEvent.builder().itemId(item.getId()).restaurantId(savedRestaurant.getRestaurantId()).itemName(item.getName()).description(item.getDescription()).price(item.getPrice()).quantity(item.getQuantity()).category(item.getCategory()).availability(item.getIsAvailable()).itemImage(item.getImageUrl()).timestamp(now().toString()).eventType(eventType).build()).toList() : null).build();
    }

    @Override
    @Transactional
    public String addRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        try {
            long restaurantsId = 0;
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
            Restaurant byEmail = this.findByEmail(restaurant.getEmail());
            if (byEmail != null && byEmail.getEmail().equalsIgnoreCase(restaurant.getEmail()))
                return "Restaurant Already Exists";

            if (restaurant.getRestaurantId() == null || restaurant.getRestaurantId() == 0L) {
                restaurantsId = sequenceService.getSequenceByCustomer("RESTAURANTS");
                restaurant.setRestaurantId(restaurantsId);
            }

            RestaurantOwner owner = RestaurantOwner.builder().ownerId(sequenceService.getSequenceByCustomer("RESTAURANT_OWNERS")).userId(currentUser.getUserId()).email(currentUser.getSecondaryEmail()).phoneNumber(currentUser.getPhoneNum()).restaurantId(restaurant.getRestaurantId()).build();
            ownerRepository.save(owner);
            restaurant.setOwner(owner);

            RestaurantLicence licence = RestaurantLicence.builder().licenceId(sequenceService.getSequenceByCustomer("RESTAURANT_LICENCES")).restaurantId(restaurant.getRestaurantId()).licenceName(currentUser.getNameAsInLicense()).nameAsInLicense(currentUser.getNameAsInLicense()).licenceNumber(currentUser.getLicenseNumber()).userId(currentUser.getUserId()).build();
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

            RestaurantDto restaurantDto = manageRestaurantDto( savedRestaurant,"Created");
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
    public String updateRestaurant( Restaurant incomingRestaurant, String authHeader) throws Exception {
        try {
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
            if (currentUser == null || !currentUser.getRole().equals("OWNER")) {
                throw new IllegalArgumentException("Only restaurant owners can update restaurant details");
            }

            Restaurant existingRestaurant = restaurantRepository.findByUserId(currentUser.getUserId());
            if(existingRestaurant == null){
                throw new IllegalArgumentException("Restaurant not found with user id: "+currentUser.getUserId());
            }

            if (incomingRestaurant.getName() != null) existingRestaurant.setName(incomingRestaurant.getName());
            if (incomingRestaurant.getDescription() != null) existingRestaurant.setDescription(incomingRestaurant.getDescription());
            if (incomingRestaurant.getContactNumber() != null) existingRestaurant.setContactNumber(incomingRestaurant.getContactNumber());
            if (incomingRestaurant.getAddress() != null) existingRestaurant.setAddress(incomingRestaurant.getAddress());
            if (incomingRestaurant.getArea() != null) existingRestaurant.setArea(incomingRestaurant.getArea());
            if (incomingRestaurant.getCity() != null) existingRestaurant.setCity(incomingRestaurant.getCity());
            if (incomingRestaurant.getState() != null) existingRestaurant.setState(incomingRestaurant.getState());
            if (incomingRestaurant.getPincode() != null) existingRestaurant.setPincode(incomingRestaurant.getPincode());
            if (incomingRestaurant.getCuisineType() != null) existingRestaurant.setCuisineType(incomingRestaurant.getCuisineType());
            if (incomingRestaurant.getOpeningTime() != null) existingRestaurant.setOpeningTime(incomingRestaurant.getOpeningTime());
            if (incomingRestaurant.getClosingTime() != null) existingRestaurant.setClosingTime(incomingRestaurant.getClosingTime());
            if (incomingRestaurant.getIsOpen() != null) existingRestaurant.setIsOpen(incomingRestaurant.getIsOpen());
            if (incomingRestaurant.getIsPureVeg() != null) existingRestaurant.setIsPureVeg(incomingRestaurant.getIsPureVeg());
            if (incomingRestaurant.getAcceptsOnlineOrders() != null) existingRestaurant.setAcceptsOnlineOrders(incomingRestaurant.getAcceptsOnlineOrders());
            if (incomingRestaurant.getAcceptsTableBooking() != null) existingRestaurant.setAcceptsTableBooking(incomingRestaurant.getAcceptsTableBooking());
            if (incomingRestaurant.getHomeDelivery() != null) existingRestaurant.setHomeDelivery(incomingRestaurant.getHomeDelivery());
            if (incomingRestaurant.getLatitude() != null) existingRestaurant.setLatitude(incomingRestaurant.getLatitude());
            if (incomingRestaurant.getLongitude() != null) existingRestaurant.setLongitude(incomingRestaurant.getLongitude());
            if (incomingRestaurant.getDeliveryTimeInMin() != null) existingRestaurant.setDeliveryTimeInMin(incomingRestaurant.getDeliveryTimeInMin());
            if (incomingRestaurant.getDeliveryCharge() != null) existingRestaurant.setDeliveryCharge(incomingRestaurant.getDeliveryCharge());
            if (incomingRestaurant.getImageUrl() != null) existingRestaurant.setImageUrl(incomingRestaurant.getImageUrl());
            if (incomingRestaurant.getPaymentMethods() != null) existingRestaurant.setPaymentMethods(incomingRestaurant.getPaymentMethods());
            if (incomingRestaurant.getAdditionalServices() != null) existingRestaurant.setAdditionalServices(incomingRestaurant.getAdditionalServices());
            if (incomingRestaurant.getSocialMediaLinks() != null) existingRestaurant.setSocialMediaLinks(incomingRestaurant.getSocialMediaLinks());
            if (incomingRestaurant.getWebsiteUrl() != null) existingRestaurant.setWebsiteUrl(incomingRestaurant.getWebsiteUrl());
            existingRestaurant.setUpdatedAt(LocalDateTime.now());

            if (incomingRestaurant.getMenuList() != null) {
                for (Menu incomingItem : incomingRestaurant.getMenuList()) {
                    if (incomingItem.getId() == null || incomingItem.getId() == 0L) {
                        incomingItem.setId(sequenceService.getSequenceByCustomer("MENU"));
                        incomingItem.setRestaurant(existingRestaurant);
                        existingRestaurant.getMenuList().add(incomingItem);
                    } else {
                        existingRestaurant.getMenuList().stream()
                                .filter(m -> m.getId().equals(incomingItem.getId()))
                                .findFirst()
                                .ifPresent(existingItem -> {
                                    if (incomingItem.getName() != null) existingItem.setName(incomingItem.getName());
                                    if (incomingItem.getDescription() != null) existingItem.setDescription(incomingItem.getDescription());
                                    if (incomingItem.getPrice() != null) existingItem.setPrice(incomingItem.getPrice());
                                    if (incomingItem.getQuantity() != null) existingItem.setQuantity(incomingItem.getQuantity());
                                    if (incomingItem.getCategory() != null) existingItem.setCategory(incomingItem.getCategory());
                                    if (incomingItem.getIsAvailable() != null) existingItem.setIsAvailable(incomingItem.getIsAvailable());
                                    if (incomingItem.getImageUrl() != null) existingItem.setImageUrl(incomingItem.getImageUrl());
                                });
                    }
                }
            }
            Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);

            RestaurantDto restaurantDto = manageRestaurantDto(updatedRestaurant, "Updated");
            publishRestaurantAfterCommit(restaurantDto);
            return "Successfully Restaurant Updated!!!";

        } catch (Exception e) {
            log.error("Exception occurred while updating restaurant  details: {}", ExceptionUtils.getStackTrace(e));
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
