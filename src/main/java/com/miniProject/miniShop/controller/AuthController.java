package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AuthRequest;
import com.miniProject.miniShop.dto.AuthResponse;
import com.miniProject.miniShop.dto.UserDto;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto request) {

        User newUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String jwt = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}