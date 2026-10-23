package com.ecommerce.catalog.infrastructure.mapper;

import com.ecommerce.catalog.domain.model.CartItem;
import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.infrastructure.driver_adapters.CartItemData;
import com.ecommerce.catalog.infrastructure.driver_adapters.ProductDataJpaRepository;
import com.ecommerce.catalog.infrastructure.driver_adapters.ShoppingCartData;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CartItemResponse;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShoppingCartMapper {

    private final ProductDataJpaRepository productDataJpaRepository;

    public ShoppingCart toShoppingCart(ShoppingCartData data) {
        List<CartItem> items = data.getItems() == null
                ? new ArrayList<>()
                : data.getItems().stream().map(this::toCartItem).toList();

        return new ShoppingCart(
                data.getId(),
                data.getUserId(),
                data.getStatus(),
                new ArrayList<>(items),
                data.getTotal()
        );
    }

    public ShoppingCartData toData(ShoppingCart cart) {
        ShoppingCartData data = new ShoppingCartData();
        data.setId(cart.getId());
        data.setUserId(cart.getUserId());
        data.setStatus(cart.getStatus());
        data.setTotal(cart.getTotal());

        List<CartItemData> itemData = cart.getItems() == null
                ? new ArrayList<>()
                : cart.getItems().stream()
                .map(item -> toData(item, data))
                .toList();

        data.setItems(new ArrayList<>(itemData));
        return data;
    }

    public CartResponse toResponse(ShoppingCart cart) {
        List<CartItemResponse> items = cart.getItems() == null
                ? List.of()
                : cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getStatus(),
                items,
                cart.getTotal()
        );
    }

    private CartItem toCartItem(CartItemData data) {
        return new CartItem(
                data.getId(),
                data.getProduct().getId(),
                data.getProduct().getName(),
                data.getUnitPrice(),
                data.getQuantity(),
                data.getSubtotal()
        );
    }

    private CartItemData toData(CartItem item, ShoppingCartData cartData) {
        CartItemData data = new CartItemData();
        data.setId(item.getId());
        data.setCart(cartData);
        data.setProduct(productDataJpaRepository.getReferenceById(item.getProductId()));
        data.setUnitPrice(item.getUnitPrice());
        data.setQuantity(item.getQuantity());
        data.setSubtotal(item.getSubtotal());
        return data;
    }
}
