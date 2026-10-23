package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.CartAlreadyPurchasedException;
import com.ecommerce.catalog.domain.exception.CartNotFoundException;
import com.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.ecommerce.catalog.domain.exception.InsufficientStockException;
import com.ecommerce.catalog.domain.exception.InvalidQuantityException;
import com.ecommerce.catalog.domain.exception.UserNotFoundException;
import com.ecommerce.catalog.domain.model.CartItem;
import com.ecommerce.catalog.domain.model.CartStatus;
import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.domain.model.gateway.AuthGateway;
import com.ecommerce.catalog.domain.model.gateway.ProductGateway;
import com.ecommerce.catalog.domain.model.gateway.ShoppingCartGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@RequiredArgsConstructor
public class ShoppingCartUseCase {

    private static final BigDecimal ZERO_TOTAL = new BigDecimal("0.00");

    private final ShoppingCartGateway shoppingCartGateway;
    private final ProductGateway productGateway;
    private final AuthGateway authGateway;

    public ShoppingCart getOrCreateActiveCart(String userId) {
        validateUser(userId);

        ShoppingCart activeCart = shoppingCartGateway.findActiveCartByUserId(userId);
        if (activeCart != null) {
            return activeCart;
        }

        ShoppingCart newCart = new ShoppingCart(
                null,
                userId,
                CartStatus.ACTIVE,
                new ArrayList<>(),
                ZERO_TOTAL
        );

        return shoppingCartGateway.saveCart(newCart);
    }

    public ShoppingCart findCartById(Long cartId) {
        return shoppingCartGateway.findCartById(cartId);
    }

    public ShoppingCart findActiveCart(String userId) {
        validateUser(userId);
        ShoppingCart activeCart = shoppingCartGateway.findActiveCartByUserId(userId);
        if (activeCart == null) {
            throw new CartNotFoundException();
        }
        return activeCart;
    }

    public ShoppingCart addProduct(Long cartId, Long productId, Integer quantity) {
        validateQuantity(quantity);
        ShoppingCart cart = shoppingCartGateway.findCartById(cartId);
        validateActiveCart(cart);

        Product product = productGateway.findProductById(productId);
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        int finalQuantity = quantity;
        if (existingItem != null) {
            finalQuantity += existingItem.getQuantity();
        }

        validateAvailableStock(product, finalQuantity);

        if (existingItem == null) {
            CartItem newItem = new CartItem(
                    null,
                    product.getId(),
                    product.getName(),
                    normalizeMoney(product.getPrice()),
                    quantity,
                    calculateSubtotal(product.getPrice(), quantity)
            );
            cart.getItems().add(newItem);
        } else {
            existingItem.setProductName(product.getName());
            existingItem.setUnitPrice(normalizeMoney(product.getPrice()));
            existingItem.setQuantity(finalQuantity);
            existingItem.setSubtotal(calculateSubtotal(product.getPrice(), finalQuantity));
        }

        recalculateTotal(cart);
        return shoppingCartGateway.saveCart(cart);
    }

    public ShoppingCart updateQuantity(Long cartId, Long productId, Integer quantity) {
        validateQuantity(quantity);
        ShoppingCart cart = shoppingCartGateway.findCartById(cartId);
        validateActiveCart(cart);

        Product product = productGateway.findProductById(productId);
        validateAvailableStock(product, quantity);

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(CartItemNotFoundException::new);

        item.setProductName(product.getName());
        item.setUnitPrice(normalizeMoney(product.getPrice()));
        item.setQuantity(quantity);
        item.setSubtotal(calculateSubtotal(product.getPrice(), quantity));

        recalculateTotal(cart);
        return shoppingCartGateway.saveCart(cart);
    }

    public ShoppingCart removeProduct(Long cartId, Long productId) {
        ShoppingCart cart = shoppingCartGateway.findCartById(cartId);
        validateActiveCart(cart);

        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            throw new CartItemNotFoundException();
        }

        recalculateTotal(cart);
        return shoppingCartGateway.saveCart(cart);
    }

    private void validateUser(String userId) {
        if (userId == null || userId.isBlank() || !authGateway.userExists(userId)) {
            throw new UserNotFoundException();
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }

    private void validateAvailableStock(Product product, int quantity) {
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new InsufficientStockException();
        }
    }

    private void validateActiveCart(ShoppingCart cart) {
        if (cart.getStatus() == CartStatus.PURCHASED) {
            throw new CartAlreadyPurchasedException();
        }
    }

    private BigDecimal calculateSubtotal(BigDecimal unitPrice, int quantity) {
        return normalizeMoney(unitPrice).multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    private void recalculateTotal(ShoppingCart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(normalizeMoney(total));
    }

    private BigDecimal normalizeMoney(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
