package com.checkout.checkout_system.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.checkout.checkout_system.model.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    
    @Query("SELECT p FROM Product p WHERE p.quantity < 5")
                List<Product> getLowStock();

}