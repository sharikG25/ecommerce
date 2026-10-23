package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.ShoppingCart;

import java.util.List;

public interface CheckoutGateway {

    ShoppingCart completePurchase(ShoppingCart shoppingCart, List<Product> products);
}
