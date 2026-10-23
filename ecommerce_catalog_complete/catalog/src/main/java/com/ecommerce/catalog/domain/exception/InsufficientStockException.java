package com.ecommerce.catalog.domain.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("No hay suficiente inventario disponible para uno o más productos.");
    }
}
