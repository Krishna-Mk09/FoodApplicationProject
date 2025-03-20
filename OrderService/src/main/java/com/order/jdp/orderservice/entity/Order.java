package com.order.jdp.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@Entity
@Table(name = "`order`")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId; //  pk
    @Column(name = "user_id")
    private long userId;  // fk
    @Column(name = "address_id")
    private long addressId; //fk
    @Column(name = "restaurant_id")
    private long noOfItems;
    @Column(name = "order_buy_date")
    private LocalDateTime orderBuyDate;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "order_discount")
    private long orderDiscount;
    @Column(name = "order_tax")
    private long orderTax;
    @Column(name = "order_total_amount")
    private long orderTotalAmount;
    @Column(name = "order_payment_id")
    private long orderPaymentId; // fk
    @Column(name = "order_delivery_id")
    private long orderDeliveryId; // fk
    @Column(name = "order_rating")
    private long orderRating;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItems> orderItems;



}
