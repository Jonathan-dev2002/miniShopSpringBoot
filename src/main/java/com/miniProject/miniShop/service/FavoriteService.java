package com.miniProject.miniShop.service;

import com.miniProject.miniShop.model.Favorite;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.FavoriteRepository;
import com.miniProject.miniShop.repository.ProductRepository;
import com.miniProject.miniShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Favorite addFavorite(String email, UUID productId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            return favoriteRepository.findByUserAndProduct(user, product).get();
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(String email, UUID productId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    public List<Favorite> getMyFavorites(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}
