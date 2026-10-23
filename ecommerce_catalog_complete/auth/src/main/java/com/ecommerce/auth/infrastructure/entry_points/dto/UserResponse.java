package com.ecommerce.auth.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record UserResponse(
        String idUser,
        String name,
        String mail,
        String role,
        Integer age,
        String phoneNumber,
        BigDecimal balance
) {
}
