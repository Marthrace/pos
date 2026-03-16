package com.checkout.checkout_system.repository;

import com.checkout.checkout_system.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}