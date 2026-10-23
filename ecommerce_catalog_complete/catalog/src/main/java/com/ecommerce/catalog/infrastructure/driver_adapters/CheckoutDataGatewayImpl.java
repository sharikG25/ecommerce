package com.ecommerce.catalog.infrastructure.driver_adapters;

import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.domain.model.gateway.CheckoutGateway;
import com.ecommerce.catalog.infrastructure.mapper.ProductMapper;
import com.ecommerce.catalog.infrastructure.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CheckoutDataGatewayImpl implements CheckoutGateway {

    private final ProductDataJpaRepository productDataJpaRepository;
    private final ShoppingCartDataJpaRepository shoppingCartDataJpaRepository;
    private final ProductMapper productMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCart completePurchase(ShoppingCart shoppingCart, List<Product> products) {
        productDataJpaRepository.saveAll(
                products.stream().map(productMapper::toData).toList()
        );

        ShoppingCartData savedCart = shoppingCartDataJpaRepository.save(
                shoppingCartMapper.toData(shoppingCart)
        );

        productDataJpaRepository.flush();
        shoppingCartDataJpaRepository.flush();

        return shoppingCartMapper.toShoppingCart(savedCart);
    }
}
