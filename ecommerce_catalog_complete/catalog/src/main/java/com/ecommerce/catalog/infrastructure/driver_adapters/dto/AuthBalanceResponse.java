package com.ecommerce.catalog.infrastructure.driver_adapters.dto;

import java.math.BigDecimal;

public record AuthBalanceResponse(
        String userId,
        BigDecimal balance
) {
}
