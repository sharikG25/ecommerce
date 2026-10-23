package com.ecommerce.catalog.domain.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("El carrito está vacío.");
    }
}
