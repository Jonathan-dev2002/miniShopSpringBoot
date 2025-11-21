package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AddressDto;
import com.miniProject.miniShop.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    // --- Admin Routes ---
    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserIdForAdmin(userId));
    }

    @PostMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAddressForUser(@PathVariable UUID userId, @RequestBody AddressDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.createAddressForAdmin(userId, request));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAddressByAdmin(@PathVariable UUID id, @RequestBody AddressDto request) {
        return ResponseEntity.ok(addressService.updateAddressByAdmin(id, request));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAddressByAdmin(@PathVariable UUID id) {
        addressService.deleteAddressByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // --- Public / User-specific Routes ---
    @PostMapping
    public ResponseEntity<?> createAddress(Authentication authentication, @RequestBody AddressDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.createAddress(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<?> getMyAddresses(Authentication authentication) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(Authentication authentication, @PathVariable UUID id, @RequestBody AddressDto request) {
        return ResponseEntity.ok(addressService.updateAddress(id, authentication.getName(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(Authentication authentication, @PathVariable UUID id) {
        addressService.deleteAddress(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<?> setDefaultAddress(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(addressService.setDefaultAddress(id, authentication.getName()));
    }
}