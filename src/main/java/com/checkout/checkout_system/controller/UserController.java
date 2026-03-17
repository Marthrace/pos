package com.checkout.checkout_system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.checkout_system.model.User;
import com.checkout.checkout_system.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role
    ) {
        return userService.createUser(username, password, role);
    }
    
}