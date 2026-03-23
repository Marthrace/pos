package com.checkout.checkout_system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.checkout_system.dto.CheckoutRequest;
import com.checkout.checkout_system.dto.ReceiptDTO;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.service.CheckoutService;
import com.checkout.checkout_system.service.ReceiptService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final ReceiptService receiptService; // add this

    public CheckoutController(CheckoutService checkoutService,
                              ReceiptService receiptService) {
        this.checkoutService = checkoutService;
        this.receiptService = receiptService;
    }

    @PostMapping
    public ReceiptDTO checkout(@RequestBody CheckoutRequest request) {
        // 1️⃣ Perform checkout and save order
        Order savedOrder = checkoutService.checkout(request);

        // 2️⃣ Generate receipt DTO for frontend
        return receiptService.getReceipt(savedOrder.getId());
    }
}
