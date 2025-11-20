package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.AddToCartRequest;
import com.miniProject.miniShop.model.Cart;
import com.miniProject.miniShop.model.CartItem;
import com.miniProject.miniShop.model.Product;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.CartItemRepository;
import com.miniProject.miniShop.repository.CartRepository;
import com.miniProject.miniShop.repository.ProductRepository;
import com.miniProject.miniShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public List<CartItem> getCartItems(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findByCartId(cart.getId());
    }

    @Transactional
    public CartItem addItemToCart(String email, AddToCartRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getOrCreateCart(user);

        // เช็คว่ามีของชิ้นนี้ในตะกร้าหรือยัง
        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            return cartItemRepository.save(newItem);
        }
    }

    public CartItem updateCartItem(UUID id, Integer quantity) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeCartItem(UUID id) {
        cartItemRepository.deleteById(id);
    }
}