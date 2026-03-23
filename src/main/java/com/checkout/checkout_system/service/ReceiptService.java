package com.checkout.checkout_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.checkout.checkout_system.dto.ReceiptDTO;
import com.checkout.checkout_system.dto.ReceiptItemDTO;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.model.OrderItem;
import com.checkout.checkout_system.repository.OrderItemRepository;
import com.checkout.checkout_system.repository.OrderRepository;

@Service
public class ReceiptService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReceiptService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public ReceiptDTO getReceipt(Long orderId) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow();

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);

        List<ReceiptItemDTO> itemDTOs =
                items.stream()
                        .map(i -> new ReceiptItemDTO(
                                i.getProduct().getName(),
                                i.getQuantity(),
                                i.getPrice()
                        ))
                        .collect(Collectors.toList());

        ReceiptDTO dto = new ReceiptDTO();

        dto.setOrderId(order.getId());
        dto.setDate(order.getOrderDate());
        dto.setItems(itemDTOs);

        dto.setTotal(order.getTotalAmount());
        dto.setPaid(order.getPaidAmount());
        dto.setChange(order.getChangeAmount());
        dto.setPaymentMethod(order.getPaymentMethod());

        return dto;
    }
}