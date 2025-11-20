package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.ProductDto;
import com.miniProject.miniShop.model.Category;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.repository.CategoryRepository;
import com.miniProject.miniShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts(UUID categoryId) {
        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        } else {
            return productRepository.findAll();
        }
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(ProductDto request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock() != null ? request.getStock() : 0);
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(UUID id, ProductDto request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStock() != null) product.setStock(request.getStock());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
