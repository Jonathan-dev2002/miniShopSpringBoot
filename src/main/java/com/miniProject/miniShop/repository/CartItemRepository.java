package com.miniProject.miniShop.repository;
import com.miniProject.miniShop.model.Cart;
import com.miniProject.miniShop.model.CartItem;
import com.miniProject.miniShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCartId(UUID cartId);
    void deleteByCartId(UUID cartId);
}