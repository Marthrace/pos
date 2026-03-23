package com.checkout.checkout_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.checkout.checkout_system.dto.CheckoutItem;
import com.checkout.checkout_system.dto.CheckoutRequest;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.model.OrderItem;
import com.checkout.checkout_system.model.Product;
import com.checkout.checkout_system.repository.OrderItemRepository;
import com.checkout.checkout_system.repository.OrderRepository;
import com.checkout.checkout_system.repository.ProductRepository;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CheckoutService(ProductRepository productRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

   @Transactional
public Order checkout(CheckoutRequest request) {

    Order order = new Order();

    double total = 0;

    order = orderRepository.save(order);

    for (CheckoutItem item : request.getItems()) {

        Product product =
                productRepository
                        .findById(item.getProductId())
                        .orElseThrow();

        int qty = item.getQuantity();

        // ✅ check stock
        if (qty > product.getQuantity()) {

            throw new RuntimeException(
                    "Not enough stock for "
                            + product.getName()
            );
        }

        // ✅ decrease stock
        int newQty =
                product.getQuantity() - qty;

        product.setQuantity(newQty);

        productRepository.save(product);

        double unitPrice = product.getPrice();

                double subtotal = unitPrice * qty;

                total += subtotal;

                OrderItem orderItem =
                        new OrderItem(
                                order,
                                product,
                                qty,
                                unitPrice   // store unit price, not subtotal
                        );
        orderItemRepository.save(orderItem);
    }

    // ✅ total
    order.setTotalAmount(total);

    // ✅ PAYMENT PART
    order.setPaymentMethod(
            request.getPaymentMethod()
    );

    order.setPaidAmount(
            request.getPaidAmount()
    );

    double change =
            request.getPaidAmount() - total;

    order.setChangeAmount(change);

    return orderRepository.save(order);
}
}