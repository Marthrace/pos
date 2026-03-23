package com.checkout.checkout_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.checkout.checkout_system.model.Product;
import com.checkout.checkout_system.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // Save or update product with validations
    public Product save(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new RuntimeException("Product name required");
        }

        if (product.getPrice() <= 0) {
            throw new RuntimeException("Invalid price");
        }

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        Optional<Product> existing = repo.findByName(product.getName());

        if (existing.isPresent()) {
            Product p = existing.get();
            p.setQuantity(p.getQuantity() + product.getQuantity());
            p.setPrice(product.getPrice());
            return repo.save(p);
        } else {
            return repo.save(product);
        }
    }

    // Get all products
    public List<Product> getAll() {
        return repo.findAll();
    }

    // **New method to get low stock products**
    public List<Product> getLowStock() {
        return repo.getLowStock();
    }
}