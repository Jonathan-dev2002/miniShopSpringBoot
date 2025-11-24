package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.CategorySearchRequest;
import com.miniProject.miniShop.model.Category;
import com.miniProject.miniShop.repository.CategoryRepository;
import com.miniProject.miniShop.spec.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //    public List<Category> getAllCategory() { return categoryRepository.findAll(); }
    public Page<Category> getAllCategory(CategorySearchRequest request) {
        // เรียงตาม createdAt ล่าสุดก่อน
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        // สร้างเงื่อนไขค้นหา
        Specification<Category> spec = CategorySpecification.hasKeyword(request.getKeyword());

        return categoryRepository.findAll(spec, pageable);
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID id, String name) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if (name != null) category.setName(name);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
