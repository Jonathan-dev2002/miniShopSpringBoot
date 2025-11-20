package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.UserDto;
import com.miniProject.miniShop.model.Role;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(UserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase())); //String -> enum obj + upper case
            } catch (IllegalArgumentException e) {
                user.setRole(Role.USER);
            }
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserDto request) {
        User user = getUserById(id);
        if (user == null) throw new RuntimeException("User not found");

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}