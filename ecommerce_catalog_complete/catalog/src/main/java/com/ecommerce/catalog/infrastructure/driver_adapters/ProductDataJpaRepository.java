package com.ecommerce.catalog.infrastructure.driver_adapters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDataJpaRepository extends JpaRepository<ProductData, Long> {
}
