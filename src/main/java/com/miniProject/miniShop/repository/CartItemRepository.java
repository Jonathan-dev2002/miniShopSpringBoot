package com.miniProject.miniShop.repository;
import com.miniProject.miniShop.model.Cart;
import com.miniProject.miniShop.model.CartItem;
import com.miniProject.miniShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCartId(UUID cartId);
    void deleteByCartId(UUID cartId);
}