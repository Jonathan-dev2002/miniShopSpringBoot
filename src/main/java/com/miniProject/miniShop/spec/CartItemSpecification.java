package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.CartItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.UUID;

public class CartItemSpecification {


    public static Specification<CartItem> hasCartId(UUID cartId) {
        return (root, query, criteriaBuilder) -> {
            if (cartId == null) return null;
            return criteriaBuilder.equal(root.get("cart").get("id"), cartId);
        };
    }

    //ค้นหาชื่อสินค้าในตะกร้า
    public static Specification<CartItem> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            // วิ่งเข้าไปหา product -> name
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), searchKey);
        };
    }
}