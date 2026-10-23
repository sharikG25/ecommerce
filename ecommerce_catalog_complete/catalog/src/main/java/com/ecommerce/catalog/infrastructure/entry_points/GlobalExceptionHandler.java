package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.exception.AuthServiceException;
import com.ecommerce.catalog.domain.exception.CartAlreadyPurchasedException;
import com.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.ecommerce.catalog.domain.exception.CartNotFoundException;
import com.ecommerce.catalog.domain.exception.CheckoutProcessingException;
import com.ecommerce.catalog.domain.exception.EmptyCartException;
import com.ecommerce.catalog.domain.exception.InsufficientBalanceException;
import com.ecommerce.catalog.domain.exception.InsufficientStockException;
import com.ecommerce.catalog.domain.exception.InvalidProductDataException;
import com.ecommerce.catalog.domain.exception.InvalidQuantityException;
import com.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.ecommerce.catalog.domain.exception.UserNotFoundException;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidProductDataException.class, InvalidQuantityException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({
            ProductNotFoundException.class,
            CartNotFoundException.class,
            CartItemNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({
            InsufficientStockException.class,
            InsufficientBalanceException.class,
            CartAlreadyPurchasedException.class,
            EmptyCartException.class
    })
    public ResponseEntity<ApiErrorResponse> handleConflict(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthService(AuthServiceException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(CheckoutProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleCheckoutProcessing(CheckoutProcessingException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("Ocurrió un error inesperado durante la operación."));
    }
}
