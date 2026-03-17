package com.checkout.checkout_system.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.checkout.checkout_system.model.Role;
import com.checkout.checkout_system.model.User;
import com.checkout.checkout_system.repository.RoleRepository;
import com.checkout.checkout_system.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder encoder;

public UserService(UserRepository userRepository,
                   RoleRepository roleRepository,
                   BCryptPasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
}
    public User createUser(String username,
                           String password,
                           String roleName) {

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

       User user = new User(
        username,
        encoder.encode(password),
        role);
        return userRepository.save(user);
    }

    }

    
