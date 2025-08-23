package com.restaurant.jdp.RestaurantService.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.restaurant.jdp.RestaurantService.config.SecurityConfig;
import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import com.restaurant.jdp.RestaurantService.entity.RestaurantLicence;
import com.restaurant.jdp.RestaurantService.entity.RestaurantOwner;
import com.restaurant.jdp.RestaurantService.repository.LicenceRepository;
import com.restaurant.jdp.RestaurantService.repository.OwnerRepository;
import com.restaurant.jdp.RestaurantService.repository.RestaurantRepository;
import com.restaurant.jdp.RestaurantService.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final LicenceRepository licenceRepository;
    private final OwnerRepository ownerRepository;
    private final SequenceService sequenceService;


    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, LicenceRepository licenceRepository, OwnerRepository ownerRepository, SequenceService sequenceService, SecurityConfig securityConfig, OwnerRepository restaurantOwnerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.licenceRepository = licenceRepository;
        this.ownerRepository = ownerRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    @Transactional
    public String addRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        try {
            UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));

            if (restaurant.getRestaurantLicence() == null) {
                restaurant.setRestaurantLicence(new RestaurantLicence());
            }
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
            if (licence.getLicenceId() == null || licence.getLicenceId() == 0L) {
                licence.setLicenceId(sequenceService.getSequenceByCustomer("RESTAURANT_LICENCES"));
            }
            licence.setRestaurantId(restaurant.getRestaurantId());
            licence.setLicenceName(currentUser.getNameAsInLicense());
            licence.setLicenceNumber(currentUser.getLicenseNumber());
            licence.setNameAsInLicense(currentUser.getNameAsInLicense());
            licence.setUserId(currentUser.getUserId());
            licenceRepository.save(licence);
            restaurant.setRestaurantLicence(licence);
            if (restaurant.getItems() != null) {
                restaurant.getItems().forEach(item -> item.setRestaurant(restaurant));
            }
            LocalDateTime now = LocalDateTime.now();
            restaurant.setUserId(currentUser.getUserId());
            restaurant.setCreatedAt(now);
            restaurant.setUpdatedAt(now);
             restaurantRepository.save(restaurant);
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

            Restaurant existingRestaurant = restaurantRepository.findById(restaurant.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurant.getRestaurantId()));
            existingRestaurant.setName(restaurant.getName());
            existingRestaurant.setDescription(restaurant.getDescription());
            existingRestaurant.setContactNumber(restaurant.getContactNumber());
            existingRestaurant.setEmail(restaurant.getEmail());
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
    public Restaurant getRestaurantById(long id) {
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return null;
    }

    @Override
    public List<Restaurant> searchRestaurants(String query) {
        return null;
    }

}
