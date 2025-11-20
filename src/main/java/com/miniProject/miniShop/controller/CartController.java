package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AddToCartRequest;
import com.miniProject.miniShop.dto.UpdateCartRequest;
import com.miniProject.miniShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

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