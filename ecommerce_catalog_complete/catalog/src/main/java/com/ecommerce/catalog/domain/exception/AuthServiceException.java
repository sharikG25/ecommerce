package com.ecommerce.catalog.domain.exception;

public class AuthServiceException extends RuntimeException {

    public AuthServiceException() {
        super("No fue posible comunicarse con el servicio de autenticación.");
    }
}
