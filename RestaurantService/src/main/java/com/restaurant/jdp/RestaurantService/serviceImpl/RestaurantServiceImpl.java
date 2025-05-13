package com.restaurant.jdp.RestaurantService.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.restaurant.jdp.RestaurantService.config.SecurityConfig;
import com.restaurant.jdp.RestaurantService.entity.Restaurant;
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
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, LicenceRepository licenceRepository, OwnerRepository ownerRepository, SequenceService sequenceService, SecurityConfig securityConfig) {
        this.restaurantRepository = restaurantRepository;
        this.licenceRepository = licenceRepository;
        this.ownerRepository = ownerRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    @Transactional
    public void addRestaurant(Restaurant restaurant, String authHeader) throws Exception {
        long restaurantId = 0L;
        long restaurantLicenseId = 0L;
        UserDTO currentUser = sequenceService.getCurrentUser(authHeader.substring(7));
        try {
            restaurant.setUserId(1233380988L);
            if (restaurant.getRestaurantLicence().getRestaurantId() == 0L) {
                restaurantId = sequenceService.getSequenceByCustomer("RESTAURANTS");
                restaurant.setRestaurantId(restaurantId);
                restaurant.getRestaurantLicence().setRestaurantId(restaurantId);
            }
            if (restaurant.getOwner() != null && restaurant.getOwner().getOwnerId() != 0) {
                RestaurantOwner owner = ownerRepository.findById(restaurant.getOwner().getRestaurantId()).orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + restaurant.getOwner().getOwnerId()));
                restaurant.setOwner(owner);
            }
            if (restaurant.getRestaurantLicence().getLicenceId() == 0L) {
                restaurantLicenseId = sequenceService.getSequenceByCustomer("RESTAURANT_LICENCES");
                restaurant.getRestaurantLicence().setLicenceId(restaurantLicenseId);
            }
            LocalDateTime now = LocalDateTime.now();
            restaurant.setCreatedAt(now);
            restaurant.setUpdatedAt(now);
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            log.error(" Exception occurred while saving restaurant details {} ", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
    }

    @Override
    public void deleteRestaurant(long id) {
        // Implementation for deleting a restaurant
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        // Implementation for getting a restaurant by ID
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        // Implementation for getting all restaurants
        return null;
    }

    @Override
    public List<Restaurant> searchRestaurants(String query) {
        // Implementation for searching restaurants based on a query
        return null;
    }

}
