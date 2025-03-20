package com.order.jdp.orderservice.repository;

import com.order.jdp.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Transactional
    @Query("update Order o set o.orderStatus = 'Cancelled' where o.orderId = :orderId")
    void cancelByOrderId(@Param("orderId") long orderId);

    List<Order> findByUserId(long userId);
}
