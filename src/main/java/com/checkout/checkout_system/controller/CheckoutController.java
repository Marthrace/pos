package com.checkout.checkout_system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.checkout_system.dto.CheckoutRequest;
import com.checkout.checkout_system.model.Order;
import com.checkout.checkout_system.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(
            CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public Order checkout(
            @RequestBody CheckoutRequest request
    ) {
        return checkoutService.checkout(request);
    }
}