package com.ticketsupport.userservice.seed;

import com.ticketsupport.userservice.model.entity.User;
import com.ticketsupport.userservice.repository.UserRepository;
import com.ticketsupport.userservice.util.PasswordUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(build("Admin", "admin@test.com", "admin123", User.Role.ADMIN));
        userRepository.save(build("Agent", "agent@test.com", "agent123", User.Role.AGENT));
        userRepository.save(build("Customer", "user@test.com", "user123", User.Role.CUSTOMER));
    }

    private User build(String name, String email, String password, User.Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        return user;
    }
}
