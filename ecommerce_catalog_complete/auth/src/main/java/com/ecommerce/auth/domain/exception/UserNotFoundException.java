package com.ecommerce.auth.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario no existe.");
    }
}
