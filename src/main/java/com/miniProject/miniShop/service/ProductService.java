package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.ProductDto;
import com.miniProject.miniShop.dto.ProductSearchRequest;
import com.miniProject.miniShop.model.Category;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.repository.CategoryRepository;
import com.miniProject.miniShop.repository.ProductRepository;
import com.miniProject.miniShop.spec.ProductSpecification;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //    public List<Product> getAllProducts(UUID categoryId) {
//        if (categoryId != null) {
//            return productRepository.findByCategoryId(categoryId);
//        } else {
//            return productRepository.findAll();
//        }
//    }
    public Page<Product> getAllProducts(ProductSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Specification<Product> spec = Specification.where(ProductSpecification.hasKeyword(request.getKeyword()))
                .and(ProductSpecification.hasCategory(request.getCategoryId()))
                .and(ProductSpecification.hasPriceRange(request.getMinPrice(), request.getMaxPrice()));

        return productRepository.findAll(spec, pageable);
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
