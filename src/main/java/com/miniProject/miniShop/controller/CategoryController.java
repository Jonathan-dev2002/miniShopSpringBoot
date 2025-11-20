package com.miniProject.miniShop.controller;

import com.miniProject.miniShop.dto.CategoryDto;
import com.miniProject.miniShop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
        var category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id, @RequestBody CategoryDto request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}