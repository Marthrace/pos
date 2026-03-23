package com.checkout.checkout_system.service;

import com.checkout.checkout_system.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> getSummary() {

    Double totalSales =
            orderRepository.getTotalSales();

    Long totalOrders =
            orderRepository.getTotalOrders();

    Double todaySales =
            orderRepository.getTodaySales();

    Map<String, Object> map =
            new HashMap<>();

    map.put("totalSales", totalSales);
    map.put("totalOrders", totalOrders);
    map.put("todaySales", todaySales);

    return map;
}
}