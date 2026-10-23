package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl
) {
}
