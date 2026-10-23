package com.ecommerce.catalog.infrastructure.driver_adapters.dto;

import java.math.BigDecimal;

public record AuthBalanceRequest(
        BigDecimal amount
) {
}
