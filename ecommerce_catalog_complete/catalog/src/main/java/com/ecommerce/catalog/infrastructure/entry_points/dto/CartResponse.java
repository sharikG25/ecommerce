package com.ecommerce.catalog.infrastructure.entry_points.dto;

import com.ecommerce.catalog.domain.model.CartStatus;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        String userId,
        CartStatus status,
        List<CartItemResponse> items,
        BigDecimal total
) {
}
