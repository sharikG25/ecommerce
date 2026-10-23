package com.ecommerce.auth.domain.exception;

public class InvalidBalanceAmountException extends RuntimeException {

    public InvalidBalanceAmountException() {
        super("El monto debe ser mayor que cero.");
    }
}
