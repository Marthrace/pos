package com.checkout.checkout_system.service;

import com.checkout.checkout_system.dto.CashierSalesItemDTO;
import com.checkout.checkout_system.dto.CashierSalesReportDTO;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.model.OrderItem;
import com.checkout.checkout_system.repository.OrderItemRepository;
import com.checkout.checkout_system.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReportService(OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // Original summary method
    public Map<String, Object> getSummary() {
        Double totalSales = orderRepository.getTotalSales();
        Long totalOrders = orderRepository.getTotalOrders();
        Double todaySales = orderRepository.getTodaySales();

        Map<String, Object> map = new HashMap<>();
        map.put("totalSales", totalSales);
        map.put("totalOrders", totalOrders);
        map.put("todaySales", todaySales);

        return map;
    }

    // ✅ Updated: Sales for a specific cashier today using repository query
    public CashierSalesReportDTO getSalesForCashier(String cashierUsername) {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // Get all orders for this cashier today using repository method
        List<Order> orders = orderRepository.findOrdersByCashierForDay(
                cashierUsername, startOfDay, endOfDay
        );

        // Flatten all order items
        List<OrderItem> allItems = orders.stream()
                .flatMap(o -> orderItemRepository.findByOrder(o).stream())
                .collect(Collectors.toList());

        // Aggregate by product
        Map<String, CashierSalesItemDTO> productMap = new HashMap<>();
        int totalItems = 0;
        double totalCash = 0;

        for (OrderItem item : allItems) {
            String productName = item.getProduct().getName();
            int qty = item.getQuantity();
            double total = item.getPrice() * qty;

            totalItems += qty;
            totalCash += total;

            productMap.compute(productName, (k, v) -> {
                if (v == null) {
                    return new CashierSalesItemDTO(productName, qty, total);
                } else {
                    v.setQuantitySold(v.getQuantitySold() + qty);
                    v.setTotal(v.getTotal() + total);
                    return v;
                }
            });
        }

        return new CashierSalesReportDTO(
                cashierUsername,
                new ArrayList<>(productMap.values()),
                totalItems,
                totalCash
        );
    }
}