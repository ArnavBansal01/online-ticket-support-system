package com.ticketsupport.userservice.model.dto.response;

import com.ticketsupport.userservice.model.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private User.Role role;
    private LocalDateTime createdAt;
    private boolean isActive;

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.email = user.getEmail();
        response.role = user.getRole();
        response.createdAt = user.getCreatedAt();
        response.isActive = user.isActive();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
