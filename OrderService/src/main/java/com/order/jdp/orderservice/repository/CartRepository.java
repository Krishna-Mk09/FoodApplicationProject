package com.order.jdp.orderservice.repository;


import com.order.jdp.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Author Name : M.V.Krishna
 * Date: 18-08-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
