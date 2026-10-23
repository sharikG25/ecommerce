package com.ecommerce.auth.infrastructure.entry_points.dto;

public record UpdateUserRequest(
        String name,
        String mail,
        String password,
        String role,
        Integer age,
        String phoneNumber
) {
}
