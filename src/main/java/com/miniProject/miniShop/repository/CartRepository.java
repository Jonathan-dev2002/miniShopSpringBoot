package com.miniProject.miniShop.repository;
import com.miniProject.miniShop.model.Cart;
import com.miniProject.miniShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
}