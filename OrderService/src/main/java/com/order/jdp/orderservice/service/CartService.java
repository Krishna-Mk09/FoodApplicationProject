package com.order.jdp.orderservice.service;

import com.order.jdp.orderservice.entity.Cart;
import com.order.jdp.orderservice.entity.Order;

public interface CartService {
    Cart getCart(long userId);
    Cart addToCart(long userId, long restaurantId, long itemId, int quantity);
    Cart updateCartItemQuantity(long cartItemId, int quantity);
    Cart removeCartItem(long cartItemId);
    void clearCart(long userId);
    Order checkoutCart(long userId, String authHeader) throws Exception;
}
