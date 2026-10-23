package com.ecommerce.auth.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record BalanceAmountRequest(
        BigDecimal amount
) {
}
