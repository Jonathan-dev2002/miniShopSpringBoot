package com.miniProject.miniShop.repository;

import com.miniProject.miniShop.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> , JpaSpecificationExecutor<Address> {
    List<Address> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Address> findByIdAndUserId(UUID id, UUID userId);
    List<Address> findByUserIdAndIsDefaultTrue(UUID userId);
}
