package com.ecommerce.auth.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("El saldo disponible es insuficiente para realizar la compra.");
    }
}
