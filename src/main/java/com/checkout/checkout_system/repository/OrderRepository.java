package com.checkout.checkout_system.repository;

import com.checkout.checkout_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalSales();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE DATE(o.orderDate) = CURRENT_DATE")
    Double getTodaySales();

    // ✅ New: get all orders for a cashier on a specific day
    @Query("SELECT o FROM Order o WHERE o.cashier.username = :username AND o.orderDate BETWEEN :start AND :end")
    List<Order> findOrdersByCashierForDay(
            @Param("username") String username,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}