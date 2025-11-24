package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.Order;
import com.miniProject.miniShop.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class OrderSpecification {

    // กรอง User
    public static Specification<Order> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    //กรอง Status
    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // ค้นหา ที่อยู่ เบอร์โทร
    public static Specification<Order> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("shippingAddress")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchKey)
            );
        };
    }
}