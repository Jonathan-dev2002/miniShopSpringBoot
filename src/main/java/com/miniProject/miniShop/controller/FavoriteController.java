package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.FavoriteDto;
import com.miniProject.miniShop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

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