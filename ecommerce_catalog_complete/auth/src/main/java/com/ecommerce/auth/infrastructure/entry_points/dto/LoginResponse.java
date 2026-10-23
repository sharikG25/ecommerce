package com.ecommerce.auth.infrastructure.entry_points.dto;

public record LoginResponse(String message, UserResponse user) {
}
