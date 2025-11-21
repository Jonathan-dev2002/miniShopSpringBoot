package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AddToCartRequest;
import com.miniProject.miniShop.dto.UpdateCartRequest;
import com.miniProject.miniShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // --- Admin Routes ---
    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCartByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
    }

    @PostMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addItemToCartByAdmin(@PathVariable UUID userId, @RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Product ID and quantity are required");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItemToCartByUserId(userId, request));
    }

    @PutMapping("/admin/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCartItemByAdmin(@PathVariable UUID id, @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(id, request.getQuantity()));
    }

    @DeleteMapping("/admin/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCartItemByAdmin(@PathVariable UUID id) {
        cartService.removeCartItem(id);
        return ResponseEntity.ok().body("Removed by admin");
    }

    // --- Public / User-specific Routes ---
    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCartItems(authentication.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(Authentication authentication, @RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Product ID and quantity are required");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItemToCart(authentication.getName(), request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID id, @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(id, request.getQuantity()));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable UUID id) {
        cartService.removeCartItem(id);
        return ResponseEntity.ok().body("Removed");
    }
}