package com.ecommerce.catalog.domain.exception;

public class CheckoutProcessingException extends RuntimeException {

    public CheckoutProcessingException(String message) {
        super(message);
    }
}
