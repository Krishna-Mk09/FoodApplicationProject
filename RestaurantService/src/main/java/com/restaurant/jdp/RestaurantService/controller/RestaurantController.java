package com.restaurant.jdp.RestaurantService.controller;

import com.restaurant.jdp.RestaurantService.entity.Restaurant;
import com.restaurant.jdp.RestaurantService.service.RestaurantService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/restaurantService")
public class RestaurantController {

    @Autowired
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    // Application admin check all restaurants list screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    // user add restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PostMapping("/restaurants")
    public void addRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.addRestaurant(restaurant);
    }

    // user search restaurant screen
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PutMapping("/restaurants/{id}")
    public void updateRestaurant(@PathVariable long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        restaurantService.updateRestaurant(restaurant);
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
