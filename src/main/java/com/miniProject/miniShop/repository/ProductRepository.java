package com.miniProject.miniShop.repository;

import com.miniProject.miniShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> , JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryId(UUID categoryId);
}
