package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.FavoriteSearchRequest;
import com.miniProject.miniShop.model.Favorite;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.FavoriteRepository;
import com.miniProject.miniShop.repository.ProductRepository;
import com.miniProject.miniShop.repository.UserRepository;
import com.miniProject.miniShop.spec.FavoriteSpecification;
import jakarta.transaction.Transactional;
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
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private Page<Favorite> getFavoritesWithPagination(UUID userId, FavoriteSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Specification<Favorite> spec = Specification.where(FavoriteSpecification.hasUserId(userId))
                .and(FavoriteSpecification.hasKeyword(request.getKeyword()));

        return favoriteRepository.findAll(spec, pageable);
    }

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

    public Favorite addFavoriteByAdmin(UUID userId, UUID productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

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

    //    public List<Favorite> getMyFavorites(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
//        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
//    }
    public Page<Favorite> getMyFavorites(String email, FavoriteSearchRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return getFavoritesWithPagination(user.getId(), request);
    }

    //    public List<Favorite> getAllFavoriteByAdmin() {
//        return favoriteRepository.findAll();
//    }
    public Page<Favorite> getAllFavoriteByAdmin(FavoriteSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        // อาจจะเพิ่ม Spec ค้นหาอื่นๆ ได้ แต่ตอนนี้เอาแค่ Keyword ไปก่อน
        Specification<Favorite> spec = FavoriteSpecification.hasKeyword(request.getKeyword());
        return favoriteRepository.findAll(spec, pageable);
    }

//    public List<Favorite> getFavoritesByUserId(UUID userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

    /// /        if(favorite.isEmpty()) {
    /// /            throw new RuntimeException("Favorite not found");
    /// /        }
    /// /        return favorite;
//    }
    public Page<Favorite> getFavoritesByUserId(UUID userId, FavoriteSearchRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return getFavoritesWithPagination(user.getId(), request);
    }

    @Transactional
    public void removeFavoriteByAdmin(UUID userId, UUID productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Favorite item not found in user's list"));
        favoriteRepository.delete(favorite);
    }
}