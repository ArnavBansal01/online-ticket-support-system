package com.ticketsupport.ticketservice.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ticketsupport.ticketservice.exception.UnauthorizedException;
import com.ticketsupport.ticketservice.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/api/tickets".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing Authorization token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtUtil.parse(token);
        request.setAttribute("userId", ((Number) claims.get("userId")).longValue());
        request.setAttribute("email", claims.get("email", String.class));
        request.setAttribute("role", claims.get("role", String.class));

        filterChain.doFilter(request, response);
    }
}
