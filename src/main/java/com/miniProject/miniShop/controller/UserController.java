package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.UpdateStatusRequest;
import com.miniProject.miniShop.dto.UserDto;
import com.miniProject.miniShop.model.Role;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- Admin Routes ---
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // ลบสำเร็จส่ง 204 No Content
    }

    @PatchMapping("/admin/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        String newRole = request.get("role");
        User updatedUser = userService.changeRole(id, Role.valueOf(newRole.toUpperCase()));
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setUserStatus(@PathVariable UUID id, @RequestBody UpdateStatusRequest request) {
        if(request.getIsActive() == null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("isActive field is required");
        }
        return ResponseEntity.ok(userService.updateUserStatus(id,request));
    }

    // --- Public / User-specific Routes ---
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<?> editCurrentUser(Authentication authentication, @RequestBody UserDto request) {
        return ResponseEntity.ok(userService.updateCurrentUser(authentication.getName(), request));
    }
}