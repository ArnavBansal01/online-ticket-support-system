package com.ticketsupport.userservice.controller;

import com.ticketsupport.userservice.model.dto.request.LoginRequest;
import com.ticketsupport.userservice.model.dto.request.RegisterRequest;
import com.ticketsupport.userservice.model.dto.response.AuthResponse;
import com.ticketsupport.userservice.model.dto.response.UserResponse;
import com.ticketsupport.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(@RequestHeader("Authorization") String authorization) {
        return authService.me(authorization);
    }
}
