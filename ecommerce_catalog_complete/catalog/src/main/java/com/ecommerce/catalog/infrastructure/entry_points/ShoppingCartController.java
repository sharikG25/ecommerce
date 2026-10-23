package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.domain.usecase.CheckoutUseCase;
import com.ecommerce.catalog.domain.usecase.ShoppingCartUseCase;
import com.ecommerce.catalog.infrastructure.entry_points.dto.AddCartItemRequest;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CartResponse;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CheckoutResponse;
import com.ecommerce.catalog.infrastructure.entry_points.dto.UpdateCartItemRequest;
import com.ecommerce.catalog.infrastructure.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartUseCase shoppingCartUseCase;
    private final CheckoutUseCase checkoutUseCase;
    private final ShoppingCartMapper shoppingCartMapper;

    @PostMapping("/users/{userId}")
    public ResponseEntity<CartResponse> getOrCreateActiveCart(@PathVariable String userId) {
        ShoppingCart cart = shoppingCartUseCase.getOrCreateActiveCart(userId);
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> findCartById(@PathVariable Long cartId) {
        ShoppingCart cart = shoppingCartUseCase.findCartById(cartId);
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @GetMapping("/users/{userId}/active")
    public ResponseEntity<CartResponse> findActiveCart(@PathVariable String userId) {
        ShoppingCart cart = shoppingCartUseCase.findActiveCart(userId);
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addProduct(
            @PathVariable Long cartId,
            @RequestBody AddCartItemRequest request
    ) {
        ShoppingCart cart = shoppingCartUseCase.addProduct(
                cartId,
                request.productId(),
                request.quantity()
        );
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequest request
    ) {
        ShoppingCart cart = shoppingCartUseCase.updateQuantity(
                cartId,
                productId,
                request.quantity()
        );
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartResponse> removeProduct(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        ShoppingCart cart = shoppingCartUseCase.removeProduct(cartId, productId);
        return ResponseEntity.ok(shoppingCartMapper.toResponse(cart));
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@PathVariable Long cartId) {
        ShoppingCart cart = checkoutUseCase.checkout(cartId);
        CheckoutResponse response = new CheckoutResponse(
                "Compra realizada exitosamente.",
                shoppingCartMapper.toResponse(cart)
        );
        return ResponseEntity.ok(response);
    }
}
