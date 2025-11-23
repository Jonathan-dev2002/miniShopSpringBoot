package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.AddressDto;
import com.miniProject.miniShop.dto.ApiResponse;
import com.miniProject.miniShop.model.Address;
import com.miniProject.miniShop.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    // --- Admin Routes ---
    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Address>>> getAddressesByUserId(@PathVariable UUID userId) {
        // return ResponseEntity.ok(addressService.getAddressesByUserIdForAdmin(userId)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(addressService.getAddressesByUserIdForAdmin(userId)));
    }

    @PostMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Address>> createAddressForUser(@PathVariable UUID userId, @RequestBody AddressDto request) {
        // return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddressForAdmin(userId, request)); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created for user successfully", addressService.createAddressForAdmin(userId, request)));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Address>> updateAddressByAdmin(@PathVariable UUID id, @RequestBody AddressDto request) {
        // return ResponseEntity.ok(addressService.updateAddressByAdmin(id, request)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully by admin", addressService.updateAddressByAdmin(id, request)));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteAddressByAdmin(@PathVariable UUID id) {
        addressService.deleteAddressByAdmin(id);
        // return ResponseEntity.noContent().build(); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully by admin", null));
    }

    // --- Public / User-specific Routes ---
    @PostMapping
    public ResponseEntity<ApiResponse<Address>> createAddress(Authentication authentication, @RequestBody AddressDto request) {
        // return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddress(authentication.getName(), request)); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created successfully", addressService.createAddress(authentication.getName(), request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Address>>> getMyAddresses(Authentication authentication) {
        // return ResponseEntity.ok(addressService.getAddressesByUserId(authentication.getName())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(addressService.getAddressesByUserId(authentication.getName())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Address>> getAddressById(Authentication authentication, @PathVariable UUID id) {
        // return ResponseEntity.ok(addressService.getAddressById(id, authentication.getName())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(addressService.getAddressById(id, authentication.getName())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(Authentication authentication, @PathVariable UUID id, @RequestBody AddressDto request) {
        // return ResponseEntity.ok(addressService.updateAddress(id, authentication.getName(), request)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", addressService.updateAddress(id, authentication.getName(), request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteAddress(Authentication authentication, @PathVariable UUID id) {
        addressService.deleteAddress(id, authentication.getName());
        // return ResponseEntity.noContent().build(); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<Address>> setDefaultAddress(Authentication authentication, @PathVariable UUID id) {
        // return ResponseEntity.ok(addressService.setDefaultAddress(id, authentication.getName())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Default address set successfully", addressService.setDefaultAddress(id, authentication.getName())));
    }
}