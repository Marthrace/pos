package com.checkout.checkout_system.controller;

import com.checkout.checkout_system.model.Product;
import com.checkout.checkout_system.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }
}