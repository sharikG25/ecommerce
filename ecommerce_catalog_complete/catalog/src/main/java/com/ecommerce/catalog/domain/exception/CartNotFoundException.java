package com.ecommerce.catalog.domain.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException() {
        super("El carrito no existe.");
    }
}
