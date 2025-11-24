package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CategorySpecification {

    public static Specification<Category> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null; // ถ้าไม่มี keyword ก็ไม่ต้องกรอง
            }
            // ค้นหาเฉพาะ field "name"
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKey);
        };
    }
}