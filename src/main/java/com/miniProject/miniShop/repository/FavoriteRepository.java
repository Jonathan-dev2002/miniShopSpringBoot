package com.miniProject.miniShop.repository;

import com.miniProject.miniShop.model.Favorite;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    boolean existsByUserAndProduct(User user, Product product);
    Optional<Favorite> findByUserAndProduct(User user, Product product);
    List<Favorite> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
