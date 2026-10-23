package com.ecommerce.catalog.domain.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException() {
        super("El producto no se encuentra en el carrito.");
    }
}
