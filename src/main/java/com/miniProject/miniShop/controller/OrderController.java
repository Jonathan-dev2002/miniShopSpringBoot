package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.CreateOrderRequest;
import com.miniProject.miniShop.dto.UpdateOrderStatusRequest;
import com.miniProject.miniShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getMyOrders(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(authentication.getName(), request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable UUID id, @RequestBody UpdateOrderStatusRequest request) {
        // ปกติตรงนี้ต้องเช็ค Role ADMIN ด้วย แต่อนุโลมให้เทสก่อน
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request.getStatus()));
    }
}