package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.UUID;

public class ProductSpecification {

    //ค้นหา Keyword
    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchKey)
            );
        };
    }

    //กรอง Category
    public static Specification<Product> hasCategory(UUID categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            // เทียบกับ category.id
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    //กรองราคา
    public static Specification<Product> hasPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return null;
        };
    }
}