package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl
) {
}
