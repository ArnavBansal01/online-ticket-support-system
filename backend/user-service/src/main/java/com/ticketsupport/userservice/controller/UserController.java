package com.ticketsupport.userservice.controller;

import com.ticketsupport.userservice.model.dto.request.UpdateRoleRequest;
import com.ticketsupport.userservice.model.dto.request.UpdateUserRequest;
import com.ticketsupport.userservice.model.dto.response.UserResponse;
import com.ticketsupport.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id, HttpServletRequest http) {
        Long requesterId = (Long) http.getAttribute("userId");
        String role = (String) http.getAttribute("role");
        return userService.getById(id, requesterId, role);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request, HttpServletRequest http) {
        Long requesterId = (Long) http.getAttribute("userId");
        String role = (String) http.getAttribute("role");
        return userService.updateUser(id, request, requesterId, role);
    }

    @GetMapping
    public List<UserResponse> listUsers(HttpServletRequest http) {
        String role = (String) http.getAttribute("role");
        return userService.listUsers(role);
    }

    @PutMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request, HttpServletRequest http) {
        String role = (String) http.getAttribute("role");
        return userService.updateRole(id, request, role);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deactivate(@PathVariable Long id, HttpServletRequest http) {
        String role = (String) http.getAttribute("role");
        userService.deactivateUser(id, role);
        return Map.of("message", "User deactivated", "deactivated", true);
    }
}
