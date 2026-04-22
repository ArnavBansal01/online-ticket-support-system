package com.ticketsupport.userservice.service;

import com.ticketsupport.userservice.model.dto.request.LoginRequest;
import com.ticketsupport.userservice.model.dto.request.RegisterRequest;
import com.ticketsupport.userservice.model.dto.response.AuthResponse;
import com.ticketsupport.userservice.model.dto.response.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse me(String token);
}
