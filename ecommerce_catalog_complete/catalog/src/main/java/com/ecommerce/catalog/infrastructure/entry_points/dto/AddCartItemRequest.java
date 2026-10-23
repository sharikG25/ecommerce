package com.ecommerce.catalog.infrastructure.entry_points.dto;

public record AddCartItemRequest(
        Long productId,
        Integer quantity
) {
}
