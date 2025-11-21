package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.FavoriteDto;
import com.miniProject.miniShop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // --- Admin Routes ---
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavoriteByAdmin());
    }

    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFavoritesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }

    @PostMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addFavoriteByAdmin(@PathVariable UUID userId, @RequestBody FavoriteDto request) {
        if (request.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is required");
        }
        // เรียก Service ตัวใหม่ที่รับ userId โดยตรง
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavoriteByAdmin(userId, request.getProductId()));
    }

    @DeleteMapping("/admin/users/{userId}/items/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeFavoriteByAdmin(@PathVariable UUID userId, @PathVariable UUID productId) {
        favoriteService.removeFavoriteByAdmin(userId, productId);
        return ResponseEntity.ok(Map.of("message", "Favorite removed successfully by admin"));
    }

    // --- Public / User-specific Routes ---
    @GetMapping
    public ResponseEntity<?> getMyFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getMyFavorites(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(Authentication authentication, @RequestBody FavoriteDto request) {
        if (request.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is required");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.addFavorite(authentication.getName(), request.getProductId()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFavorite(Authentication authentication, @PathVariable UUID productId) {
        favoriteService.removeFavorite(authentication.getName(), productId);
        return ResponseEntity.ok(Map.of("message", "Favorite removed successfully"));
    }
}