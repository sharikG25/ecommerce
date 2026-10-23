package com.ecommerce.auth.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Ya existe un usuario registrado con este correo electrónico.");
    }
}
