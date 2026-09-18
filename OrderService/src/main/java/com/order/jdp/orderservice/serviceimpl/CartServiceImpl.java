package com.order.jdp.orderservice.serviceimpl;

import com.order.jdp.orderservice.entity.Cart;
import com.order.jdp.orderservice.entity.CartItem;
import com.order.jdp.orderservice.entity.ItemsInfo;
import com.order.jdp.orderservice.entity.Order;
import com.order.jdp.orderservice.entity.OrderItems;
import com.order.jdp.orderservice.repository.CartItemRepository;
import com.order.jdp.orderservice.repository.CartRepository;
import com.order.jdp.orderservice.repository.ItemsInfoRepository;
import com.order.jdp.orderservice.service.CartService;
import com.order.jdp.orderservice.service.OrderService;
import com.order.jdp.orderservice.client.PaymentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemsInfoRepository itemsInfoRepository;
    private final OrderService orderService;
    private final PaymentClient paymentClient;

    @Override
    public Cart getCart(long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalAmount(0.0);
            cart.setRestaurantId(0L); // Default
            return cartRepository.save(cart);
        });
    }

    @Override
    @Transactional
    public Cart addToCart(long userId, long restaurantId, long itemId, int quantity) {
        Cart cart = getCart(userId);

        if (cart.getRestaurantId() != 0L && cart.getRestaurantId() != restaurantId) {
            log.info("User {} added item from different restaurant. Clearing previous cart.", userId);
            cart.getCartItems().clear();
            cart.setTotalAmount(0.0);
            cart.setRestaurantId(restaurantId);
        } else if (cart.getRestaurantId() == 0L) {
            cart.setRestaurantId(restaurantId);
        }

        ItemsInfo item = itemsInfoRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(ci -> ci.getRestaurantItemId() == itemId)
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existing = existingItemOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            existing.setTotalPrice(existing.getQuantity() * existing.getItemPrice());
        } else {
            CartItem newItem = CartItem.builder()
                    .restaurantItemId(itemId)
                    .itemName(item.getName())
                    .itemPrice(item.getPrice())
                    .quantity(quantity)
                    .totalPrice(item.getPrice() * quantity)
                    .cart(cart)
                    .build();
            cart.getCartItems().add(newItem);
        }

        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCartItemQuantity(long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        
        if (quantity <= 0) {
            return removeCartItem(cartItemId);
        }

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * cartItem.getItemPrice());
        Cart cart = cartItem.getCart();
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart removeCartItem(long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));
        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        recalculateTotal(cart);
        if (cart.getCartItems().isEmpty()) {
            cart.setRestaurantId(0L);
        }
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getCartItems().clear();
            cart.setTotalAmount(0.0);
            cart.setRestaurantId(0L);
            cartRepository.save(cart);
        });
    }

    @Override
    @Transactional
    public Order checkoutCart(long userId, String authHeader) throws Exception {
        Cart cart = getCart(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderBuyDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");
        order.setOrderTotalAmount(cart.getTotalAmount().longValue());
        
        List<OrderItems> orderItems = new ArrayList<>();
        for (CartItem ci : cart.getCartItems()) {
            OrderItems oi = new OrderItems();
            oi.setOrderItemName(ci.getItemName());
            oi.setRestaurantId(cart.getRestaurantId());
            oi.setRestaurantItemId(ci.getRestaurantItemId());
            oi.setOrderItemQuantity(ci.getQuantity());
            oi.setOrderItemPrice((long) ci.getItemPrice());
            oi.setOrderItemTotalAmount((long) ci.getTotalPrice());
            orderItems.add(oi);
        }
        order.setOrderItems(orderItems);

        // Generate Razorpay Order
        try {
            int amountInPaise = (int) (order.getOrderTotalAmount() * 100);
            java.util.Map<String, Object> paymentResponse = paymentClient.createOrder(amountInPaise, "INR", "receipt_" + userId + "_" + System.currentTimeMillis());
            if ("success".equals(paymentResponse.get("status"))) {
                order.setRazorpayOrderId((String) paymentResponse.get("orderId"));
            } else {
                log.error("Failed to generate Razorpay order: {}", paymentResponse.get("message"));
            }
        } catch (Exception e) {
            log.error("Exception calling PaymentService: {}", e.getMessage());
        }

        Order savedOrder = orderService.createOrder(order);
        clearCart(userId);
        return savedOrder;
    }

    private void recalculateTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalAmount(total);
    }
}
