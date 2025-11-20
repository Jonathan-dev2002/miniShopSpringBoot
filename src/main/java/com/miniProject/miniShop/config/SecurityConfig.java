package com.miniProject.miniShop.config;

import com.miniProject.miniShop.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration //คลาสนี้คือไฟล์สำหรับตั้งค่
@EnableWebSecurity //เปิดใช้งาน ระบบ Spring Security ทั้งหมด
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ปิด CSRF (Lambda Expression)
                .authorizeHttpRequests(auth -> auth
                        // อนุญาตให้เข้าถึง API เหล่านี้ได้โดยไม่ต้องมีการตรวจสอบ (Login/Register)
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // สำหรับ API อื่นๆ ทั้งหมด ต้องมีการตรวจสอบ (JWT)
                        .anyRequest().authenticated()
                )
                // บอก Spring Security ไม่ให้สร้างและใช้ Session (State-less)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // เพิ่ม Filter ของเรา (JwtRequestFilter) ก่อนการตรวจสอบ Username/Password ปกติ
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

