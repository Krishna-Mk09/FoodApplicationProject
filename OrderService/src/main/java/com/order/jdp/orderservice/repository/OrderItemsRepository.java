package com.order.jdp.orderservice.repository;

import com.order.jdp.orderservice.dto.OrderItemsDTO;
import com.order.jdp.orderservice.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    @Query("SELECT oi FROM OrderItems oi WHERE oi.orderId = :orderId")
    List<OrderItemsDTO> findByOrderId(@Param("orderId") long orderId);


}
