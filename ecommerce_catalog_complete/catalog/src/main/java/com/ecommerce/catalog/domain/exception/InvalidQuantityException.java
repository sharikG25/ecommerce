package com.ecommerce.catalog.domain.exception;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException() {
        super("La cantidad debe ser mayor que cero.");
    }
}
