package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.Favorite;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.UUID;

public class FavoriteSpecification {

    //กรอง User
    public static Specification<Favorite> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    //้นหาชื่อสินค้าใน Fav
    public static Specification<Favorite> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), searchKey);
        };
    }
}