package com.ticketsupport.userservice.model.dto.request;

import com.ticketsupport.userservice.model.entity.User;
import jakarta.validation.constraints.NotNull;

public class UpdateRoleRequest {
    @NotNull
    private User.Role role;

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
