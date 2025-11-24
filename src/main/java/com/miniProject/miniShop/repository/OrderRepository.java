package com.miniProject.miniShop.repository;
import com.miniProject.miniShop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Order> findByIdAndUserId(UUID id, UUID userId);
}