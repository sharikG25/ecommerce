package com.ecommerce.catalog.infrastructure.driver_adapters;

import com.ecommerce.catalog.domain.model.CartStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartDataJpaRepository extends JpaRepository<ShoppingCartData, Long> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<ShoppingCartData> findById(Long id);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<ShoppingCartData> findFirstByUserIdAndStatus(String userId, CartStatus status);
}
