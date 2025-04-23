package com.shopnow.repository;

import com.shopnow.model.Order;
import com.shopnow.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByStatus(OrderStatus status);

    List<Order> findAllByUserId(Integer userId);

    List<Order> findByUserId(Integer userId);
}