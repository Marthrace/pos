package com.checkout.checkout_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.checkout.checkout_system.model.Order;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalSales();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();
}