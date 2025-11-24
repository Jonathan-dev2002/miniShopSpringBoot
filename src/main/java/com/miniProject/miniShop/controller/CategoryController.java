package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.ApiResponse;
import com.miniProject.miniShop.dto.CategoryDto;
import com.miniProject.miniShop.dto.CategorySearchRequest;
import com.miniProject.miniShop.model.Category;
import com.miniProject.miniShop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //    @GetMapping
//    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
//        // return ResponseEntity.ok(categoryService.getAllCategory()); // Code เก่า
//        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategory()));
//    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Category>>> getAllCategories(@ModelAttribute CategorySearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategory(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
        var category = categoryService.getCategoryById(id);
        if (category == null) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found"); // Code เก่า
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Category not found"));
        }
        // return ResponseEntity.ok(category); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    // --- Admin Routes ---
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CategoryDto request) {
        // return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request.getName())); // Code เก่า
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", categoryService.createCategory(request.getName())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable UUID id, @RequestBody CategoryDto request) {
        // return ResponseEntity.ok(categoryService.updateCategory(id, request.getName())); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", categoryService.updateCategory(id, request.getName())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        // return ResponseEntity.noContent().build(); // Code เก่า
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}