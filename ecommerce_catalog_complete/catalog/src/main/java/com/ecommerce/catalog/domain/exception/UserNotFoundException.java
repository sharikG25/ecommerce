package com.ecommerce.catalog.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario no existe.");
    }
}
