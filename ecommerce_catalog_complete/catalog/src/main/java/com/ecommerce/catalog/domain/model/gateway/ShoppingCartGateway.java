package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.ShoppingCart;

public interface ShoppingCartGateway {

    ShoppingCart saveCart(ShoppingCart shoppingCart);

    ShoppingCart findCartById(Long cartId);

    ShoppingCart findActiveCartByUserId(String userId);
}
