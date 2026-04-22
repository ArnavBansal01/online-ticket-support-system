package com.ticketsupport.userservice.service.impl;

import com.ticketsupport.userservice.exception.ForbiddenException;
import com.ticketsupport.userservice.exception.ResourceNotFoundException;
import com.ticketsupport.userservice.model.dto.request.UpdateRoleRequest;
import com.ticketsupport.userservice.model.dto.request.UpdateUserRequest;
import com.ticketsupport.userservice.model.dto.response.UserResponse;
import com.ticketsupport.userservice.model.entity.User;
import com.ticketsupport.userservice.repository.UserRepository;
import com.ticketsupport.userservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getById(Long id, Long requesterId, String requesterRole) {
        boolean allowed = requesterRole.equals(User.Role.ADMIN.name())
                || requesterRole.equals(User.Role.AGENT.name())
                || requesterId.equals(id);
        if (!allowed) {
            throw new ForbiddenException("Not allowed to access this user");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request, Long requesterId, String requesterRole) {
        boolean canUpdate = requesterRole.equals(User.Role.ADMIN.name()) || requesterId.equals(id);
        if (!canUpdate) {
            throw new ForbiddenException("Not allowed to update this user");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public List<UserResponse> listUsers(String requesterRole) {
        ensureAdmin(requesterRole);
        return userRepository.findAll().stream().map(UserResponse::fromEntity).toList();
    }

    @Override
    public UserResponse updateRole(Long id, UpdateRoleRequest request, String requesterRole) {
        ensureAdmin(requesterRole);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public void deactivateUser(Long id, String requesterRole) {
        ensureAdmin(requesterRole);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    private void ensureAdmin(String requesterRole) {
        if (!User.Role.ADMIN.name().equals(requesterRole)) {
            throw new ForbiddenException("Admin role required");
        }
    }
}
