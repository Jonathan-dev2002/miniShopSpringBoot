package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.ApiResponse;
import com.miniProject.miniShop.dto.ProductDto;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(@RequestParam(required = false) UUID categoryId) {
        List<Product> products = productService.getAllProducts(categoryId);

        // return ResponseEntity.ok(productService.getAllProducts(categoryId)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        var product = productService.getProductById(id);
        if (product == null) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // Code เก่า
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Product not found"));
        }

        // return ResponseEntity.ok(product); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    // --- Admin Routes ---
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductDto request) {
        Product newProduct = productService.createProduct(request);

        // return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request)); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Product created successfully", newProduct));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody ProductDto request) {
        Product updatedProduct = productService.updateProduct(id, request);

        // return ResponseEntity.ok(productService.updateProduct(id, request)); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);

        // return ResponseEntity.noContent().build(); // Code เก่า (noContent ไม่ส่ง Body กลับ)
        // เปลี่ยนเป็นส่ง OK พร้อมข้อความแจ้งเตือนแทน เพื่อให้ Frontend รับรู้ผ่าน ApiResponse
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}