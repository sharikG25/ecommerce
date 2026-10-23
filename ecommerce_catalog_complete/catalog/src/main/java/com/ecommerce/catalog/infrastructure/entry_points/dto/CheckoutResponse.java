package com.ecommerce.catalog.infrastructure.entry_points.dto;

public record CheckoutResponse(
        String message,
        CartResponse cart
) {
}
