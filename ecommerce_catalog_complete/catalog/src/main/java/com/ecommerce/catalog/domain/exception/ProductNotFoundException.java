package com.ecommerce.catalog.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("El producto solicitado no existe.");
    }
}
