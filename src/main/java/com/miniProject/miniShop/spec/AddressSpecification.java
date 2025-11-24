package com.miniProject.miniShop.spec;

import com.miniProject.miniShop.model.Address;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.UUID;

public class AddressSpecification {

    public static Specification<Address> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    //ค้นหา ชื่อผู้รับ เบอร์ ถนน จังหวัด รหัสไปรษณีย์
    public static Specification<Address> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String searchKey = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientName")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("street")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("province")), searchKey),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("postalCode")), searchKey)
            );
        };
    }
}