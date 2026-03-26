package com.order.jdp.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@Entity
@Table(name = "`ORDERS`")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "ORDER_ID")
    private long orderId;
    @Column(name = "USER_ID")
    private long userId;
    @Column(name = "ADDRESS_ID")
    private long addressId;
    @Column(name = "RESTAURANT_ID")
    private long noOfItems;
    @Column(name = "ORDER_BUY_DATE")
    private LocalDateTime orderBuyDate;
    @Column(name = "ORDER_STATUS")
    private String orderStatus;
    @Column(name = "ORDER_DISCOUNT")
    private long orderDiscount;
    @Column(name = "ORDER_TAX")
    private long orderTax;
    @Column(name = "ORDER_TOTAL_AMOUNT")
    private long orderTotalAmount;
    @Column(name = "ORDER_PAYMENT_ID")
    private long orderPaymentId;
    @Column(name = "ORDER_DELIVERY_ID")
    private long orderDeliveryId;
    @Column(name = "ORDER_RATING")
    private long orderRating;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<OrderItems> orderItems;
    
}
