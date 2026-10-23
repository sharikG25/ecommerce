package com.ecommerce.catalog.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("El saldo disponible es insuficiente para realizar la compra.");
    }
}
