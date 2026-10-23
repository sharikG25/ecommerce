package com.ecommerce.catalog.domain.exception;

public class CartAlreadyPurchasedException extends RuntimeException {

    public CartAlreadyPurchasedException() {
        super("Esta compra ya fue procesada.");
    }
}
