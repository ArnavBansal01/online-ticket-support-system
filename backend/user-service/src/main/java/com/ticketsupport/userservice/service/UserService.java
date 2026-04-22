package com.ticketsupport.userservice.service;

import com.ticketsupport.userservice.model.dto.request.UpdateRoleRequest;
import com.ticketsupport.userservice.model.dto.request.UpdateUserRequest;
import com.ticketsupport.userservice.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getById(Long id, Long requesterId, String requesterRole);

    UserResponse updateUser(Long id, UpdateUserRequest request, Long requesterId, String requesterRole);

    List<UserResponse> listUsers(String requesterRole);

    UserResponse updateRole(Long id, UpdateRoleRequest request, String requesterRole);

    void deactivateUser(Long id, String requesterRole);
}
