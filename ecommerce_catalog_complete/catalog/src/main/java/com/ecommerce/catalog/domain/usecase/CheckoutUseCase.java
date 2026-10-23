package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.CartAlreadyPurchasedException;
import com.ecommerce.catalog.domain.exception.CheckoutProcessingException;
import com.ecommerce.catalog.domain.exception.EmptyCartException;
import com.ecommerce.catalog.domain.exception.InsufficientBalanceException;
import com.ecommerce.catalog.domain.exception.InsufficientStockException;
import com.ecommerce.catalog.domain.exception.InvalidQuantityException;
import com.ecommerce.catalog.domain.exception.UserNotFoundException;
import com.ecommerce.catalog.domain.model.CartItem;
import com.ecommerce.catalog.domain.model.CartStatus;
import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.domain.model.gateway.AuthGateway;
import com.ecommerce.catalog.domain.model.gateway.CheckoutGateway;
import com.ecommerce.catalog.domain.model.gateway.ProductGateway;
import com.ecommerce.catalog.domain.model.gateway.ShoppingCartGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CheckoutUseCase {

    private final ShoppingCartGateway shoppingCartGateway;
    private final ProductGateway productGateway;
    private final AuthGateway authGateway;
    private final CheckoutGateway checkoutGateway;

    public ShoppingCart checkout(Long cartId) {
        ShoppingCart cart = shoppingCartGateway.findCartById(cartId);

        if (cart.getStatus() == CartStatus.PURCHASED) {
            throw new CartAlreadyPurchasedException();
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        List<Product> productsToUpdate = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            validateQuantity(item.getQuantity());
            Product product = productGateway.findProductById(item.getProductId());

            if (product.getStock() == null || product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException();
            }

            BigDecimal currentPrice = normalizeMoney(product.getPrice());
            BigDecimal subtotal = currentPrice
                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            item.setProductName(product.getName());
            item.setUnitPrice(currentPrice);
            item.setSubtotal(subtotal);
            total = total.add(subtotal);

            product.setStock(product.getStock() - item.getQuantity());
            productsToUpdate.add(product);
        }

        total = normalizeMoney(total);
        cart.setTotal(total);

        if (!authGateway.userExists(cart.getUserId())) {
            throw new UserNotFoundException();
        }

        BigDecimal balance = authGateway.getBalance(cart.getUserId());
        if (balance.compareTo(total) < 0) {
            throw new InsufficientBalanceException();
        }

        authGateway.debitBalance(cart.getUserId(), total);
        cart.setStatus(CartStatus.PURCHASED);

        try {
            return checkoutGateway.completePurchase(cart, productsToUpdate);
        } catch (RuntimeException exception) {
            compensateBalance(cart.getUserId(), total);
            throw new CheckoutProcessingException("No fue posible completar la compra. El saldo fue restaurado.");
        }
    }

    private void compensateBalance(String userId, BigDecimal amount) {
        try {
            authGateway.rechargeBalance(userId, amount);
        } catch (RuntimeException exception) {
            throw new CheckoutProcessingException(
                    "No fue posible completar la compra y no fue posible confirmar la restauración del saldo. Verifica el saldo del usuario antes de reintentar."
            );
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }

    private BigDecimal normalizeMoney(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
