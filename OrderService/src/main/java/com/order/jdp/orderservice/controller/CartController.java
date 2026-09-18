package com.order.jdp.orderservice.controller;

import com.order.jdp.orderservice.entity.Cart;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable long userId) {
        log.info("Fetching cart for userId: {}", userId);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable long userId,
            @RequestParam long restaurantId,
            @RequestParam long itemId,
            @RequestParam int quantity) {
        log.info("Adding itemId {} (qty: {}) to cart for userId: {}", itemId, quantity, userId);
        return ResponseEntity.ok(cartService.addToCart(userId, restaurantId, itemId, quantity));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable long cartItemId,
            @RequestParam int quantity) {
        log.info("Updating cartItemId {} to quantity {}", cartItemId, quantity);
        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable long cartItemId) {
        log.info("Removing cartItemId: {}", cartItemId);
        return ResponseEntity.ok(cartService.removeCartItem(cartItemId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable long userId) {
        log.info("Clearing cart for userId: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Order> checkout(
            @PathVariable long userId,
            @RequestHeader("Authorization") String authHeader) throws Exception {
        log.info("Checking out cart for userId: {}", userId);
        Order order = cartService.checkoutCart(userId, authHeader);
        return ResponseEntity.ok(order);
    }
}
