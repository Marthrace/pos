package com.checkout.checkout_system.controller;

import org.springframework.web.bind.annotation.*;

import com.checkout.checkout_system.dto.ReceiptDTO;
import com.checkout.checkout_system.service.ReceiptService;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptService service;

    public ReceiptController(
            ReceiptService service) {

        this.service = service;
    }

    @GetMapping("/{id}")
    public ReceiptDTO receipt(
            @PathVariable Long id) {

        return service.getReceipt(id);
    }
}