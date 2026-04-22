package com.ticketsupport.userservice.service.impl;

import com.ticketsupport.userservice.exception.ResourceNotFoundException;
import com.ticketsupport.userservice.exception.UnauthorizedException;
import com.ticketsupport.userservice.model.dto.request.LoginRequest;
import com.ticketsupport.userservice.model.dto.request.RegisterRequest;
import com.ticketsupport.userservice.model.dto.response.AuthResponse;
import com.ticketsupport.userservice.model.dto.response.UserResponse;
import com.ticketsupport.userservice.model.entity.User;
import com.ticketsupport.userservice.repository.UserRepository;
import com.ticketsupport.userservice.service.AuthService;
import com.ticketsupport.userservice.util.JwtUtil;
import com.ticketsupport.userservice.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new UnauthorizedException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(PasswordUtil.hash(request.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        User saved = userRepository.save(user);
        String token = jwtUtil.generate(saved);
        return new AuthResponse(token, UserResponse.fromEntity(saved));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.isActive() || !user.getPasswordHash().equals(PasswordUtil.hash(request.getPassword()))) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtUtil.generate(user);
        return new AuthResponse(token, UserResponse.fromEntity(user));
    }

    @Override
    public UserResponse me(String token) {
        String raw = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = ((Number) jwtUtil.parse(raw).get("userId")).longValue();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }
}
