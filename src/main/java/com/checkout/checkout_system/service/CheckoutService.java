package com.checkout.checkout_system.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.checkout.checkout_system.dto.CheckoutItem;
import com.checkout.checkout_system.dto.CheckoutRequest;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.model.OrderItem;
import com.checkout.checkout_system.model.Product;
import com.checkout.checkout_system.model.User;
import com.checkout.checkout_system.repository.OrderItemRepository;
import com.checkout.checkout_system.repository.OrderRepository;
import com.checkout.checkout_system.repository.ProductRepository;
import com.checkout.checkout_system.repository.UserRepository;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public CheckoutService(ProductRepository productRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order checkout(CheckoutRequest request) {

        // ✅ Get logged-in cashier
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        User cashier = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        Order order = new Order();
        order.setCashier(cashier); // ✅ associate cashier with order

        double total = 0;
        order = orderRepository.save(order);

        for (CheckoutItem item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int qty = item.getQuantity();

            // ✅ check stock
            if (qty > product.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getName());
            }

            // ✅ decrease stock
            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);

            double unitPrice = product.getPrice();
            total += unitPrice * qty;

            OrderItem orderItem = new OrderItem(order, product, qty, unitPrice);
            orderItemRepository.save(orderItem);
        }

        // ✅ set totals and payment
        order.setTotalAmount(total);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaidAmount(request.getPaidAmount());
        order.setChangeAmount(request.getPaidAmount() - total);

        return orderRepository.save(order);
    }
}