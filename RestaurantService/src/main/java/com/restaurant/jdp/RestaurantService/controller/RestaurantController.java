package com.restaurant.jdp.RestaurantService.controller;

import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import com.restaurant.jdp.RestaurantService.service.RestaurantService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/restaurantService")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final SequenceService sequenceService;


    public RestaurantController(RestaurantService restaurantService, SequenceService sequenceService) {
        this.restaurantService = restaurantService;
        this.sequenceService = sequenceService;
    }

    // Application admin check all restaurants list screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    // user add restaurant screen
    @PreAuthorize("hasRole('OWNER')")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PostMapping("/addRestaurant")
    public void addRestaurant(@RequestBody Restaurant restaurant, @RequestHeader("Authorization") String authHeader) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("User Controller.login: user: ");
            restaurantService.addRestaurant(restaurant,authHeader);
        } catch (Exception e) {

            log.error(" Exception occurred while user log in ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

    // user update restaurant screen
    @PreAuthorize("hasRole('OWNER')")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PutMapping("/restaurants/{id}")
    public void updateRestaurant(@PathVariable long id, @RequestBody Restaurant restaurant, @RequestHeader("Authorization") String authHeader) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            restaurant.setRestaurantId(id);
            restaurantService.updateRestaurant(restaurant, authHeader);
        } catch (Exception e) {
            log.error("Exception occurred while updating restaurant: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // user delete restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @DeleteMapping("/restaurants/{id}")
    public void deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);
    }

    // user search restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @GetMapping("/restaurants/search")
    public List<Restaurant> searchRestaurants(@RequestParam("") String query) {
        return restaurantService.searchRestaurants(query);
    }

    // user search restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @GetMapping("/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable long id) {
        return restaurantService.getRestaurantById(id);
    }

    // user search restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @GetMapping("/restaurants/search/{query}")
    public List<Restaurant> searchRestaurantsByQuery(@PathVariable String query) {
        return restaurantService.searchRestaurants(query);
    }
//    // user search restaurant screen
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
//    @GetMapping("/restaurants/search/{query}/{cuisineType}")
//    public List<Restaurant> searchRestaurantsByQueryAndCuisineType(@PathVariable String query, @PathVariable String cuisineType) {
//        return restaurantService.searchRestaurants(query, cuisineType);
//    }
//    // user search restaurant screen
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
//    @GetMapping("/restaurants/search/{query}/{cuisineType}/{rating}")
//    public List<Restaurant> searchRestaurantsByQueryCuisineTypeAndRating(@PathVariable String query, @PathVariable String cuisineType, @PathVariable double rating) {
//        return restaurantService.searchRestaurants(query, cuisineType, rating);
//    }
//    // user search restaurant screen
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
//    @GetMapping("/restaurants/search/{query}/{cuisineType}/{rating}/{averageCostForTwo}")
//    public List<Restaurant> searchRestaurantsByQueryCuisineTypeRatingAndAverageCost(@PathVariable String query, @PathVariable String cuisineType, @PathVariable double rating, @PathVariable double averageCostForTwo) {
//        return restaurantService.searchRestaurants(query, cuisineType, rating, averageCostForTwo);
//    }


}
