package com.ecommerce.auth.domain.model.gateway;

public interface PasswordEncoderGateway {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
