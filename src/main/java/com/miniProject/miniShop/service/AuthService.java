package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.UserDto;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.UserRepository;
import com.miniProject.miniShop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

//    public AuthService(UserRepository userRepository, UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
//        this.userRepository = userRepository;
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//        this.authenticationManager = authenticationManager;
//    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
//        if(user.getIsActive() == false) {
//            throw new RuntimeException("User is disabled");
//        }
        if (Boolean.FALSE.equals(user.getIsActive())) { // เผื่อส่งค่ามาเป้น null
            throw new RuntimeException("Account is disabled");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        //ให้สร้าง Token
        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException("Login failed");
        }
    }

    public User register(UserDto request) {
        return userService.createUser(request);
    }
}
