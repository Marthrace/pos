package com.checkout.checkout_system.config;

import com.checkout.checkout_system.model.Role;
import com.checkout.checkout_system.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleLoader {

    @Bean
    CommandLineRunner loadRoles(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
            }

            if (roleRepository.findByName("MANAGER").isEmpty()) {
                roleRepository.save(new Role("MANAGER"));
            }

            if (roleRepository.findByName("CASHIER").isEmpty()) {
                roleRepository.save(new Role("CASHIER"));
            }

        };
    }
}