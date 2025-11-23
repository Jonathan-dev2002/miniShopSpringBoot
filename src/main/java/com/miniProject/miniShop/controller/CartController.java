package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AddToCartRequest;
import com.miniProject.miniShop.dto.ApiResponse;
import com.miniProject.miniShop.dto.UpdateCartRequest;
import com.miniProject.miniShop.model.CartItem;
import com.miniProject.miniShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // --- Admin Routes ---
    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartByUserId(@PathVariable UUID userId) {
        // return ResponseEntity.ok(cartService.getCartItemsByUserId(userId)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(cartService.getCartItemsByUserId(userId)));
    }

    @PostMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> addItemToCartByAdmin(@PathVariable UUID userId, @RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() == null) {
            // return ResponseEntity.badRequest().body("Product ID and quantity are required"); // Code เก่า
            return ResponseEntity.badRequest().body(ApiResponse.error("Product ID and quantity are required"));
        }
        // return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCartByUserId(userId, request)); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully by admin", cartService.addItemToCartByUserId(userId, request)));
    }

    @PutMapping("/admin/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItemByAdmin(@PathVariable UUID id, @RequestBody UpdateCartRequest request) {
        // return ResponseEntity.ok(cartService.updateCartItem(id, request.getQuantity())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully by admin", cartService.updateCartItem(id, request.getQuantity())));
    }

    @DeleteMapping("/admin/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> removeCartItemByAdmin(@PathVariable UUID id) {
        cartService.removeCartItem(id);
        // return ResponseEntity.ok().body("Removed by admin"); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Removed by admin", null));
    }

    // --- Public / User-specific Routes ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItem>>> getCart(Authentication authentication) {
        // return ResponseEntity.ok(cartService.getCartItems(authentication.getName())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(cartService.getCartItems(authentication.getName())));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Object>> addToCart(Authentication authentication, @RequestBody AddToCartRequest request) {
        if (request.getProductId() == null || request.getQuantity() == null) {
            // return ResponseEntity.badRequest().body("Product ID and quantity are required"); // Code เก่า
            return ResponseEntity.badRequest().body(ApiResponse.error("Product ID and quantity are required"));
        }
        // return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(authentication.getName(), request)); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully", cartService.addItemToCart(authentication.getName(), request)));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(@PathVariable UUID id, @RequestBody UpdateCartRequest request) {
        // return ResponseEntity.ok(cartService.updateCartItem(id, request.getQuantity())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cartService.updateCartItem(id, request.getQuantity())));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Object>> removeFromCart(@PathVariable UUID id) {
        cartService.removeCartItem(id);
        // return ResponseEntity.ok().body("Removed"); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Removed", null));
    }
}