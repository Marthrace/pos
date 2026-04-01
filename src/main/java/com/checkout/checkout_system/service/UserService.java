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

        // ✅ Check if username already exists
    if (userRepository.findByUsername(username).isPresent()) {
        throw new RuntimeException("Username '" + username + "' already exists");
    }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User(
                username,
                encoder.encode(password),
                role);

        return userRepository.save(user);
    }

    // ✅ NEW METHOD
   public User resetPassword(String adminUsername,
                          String username,
                          String newPassword) {

    // check admin user
    User admin = userRepository.findByUsername(adminUsername)
            .orElseThrow(() -> new RuntimeException("Admin not found"));

    if (!admin.getRole().getName().equalsIgnoreCase("admin")) {
        throw new RuntimeException("Only admin can reset password");
    }

    // find user to reset
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setPassword(encoder.encode(newPassword));

    return userRepository.save(user);
}
}