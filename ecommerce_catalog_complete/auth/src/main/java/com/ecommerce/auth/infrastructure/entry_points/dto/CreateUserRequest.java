package com.ecommerce.auth.infrastructure.entry_points.dto;

public record CreateUserRequest(
        String name,
        String mail,
        String password,
        String role,
        Integer age,
        String phoneNumber
) {
}
