package com.ecommerce.catalog.infrastructure.driver_adapters;

import com.ecommerce.catalog.domain.exception.CartNotFoundException;
import com.ecommerce.catalog.domain.model.CartStatus;
import com.ecommerce.catalog.domain.model.ShoppingCart;
import com.ecommerce.catalog.domain.model.gateway.ShoppingCartGateway;
import com.ecommerce.catalog.infrastructure.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ShoppingCartDataGatewayImpl implements ShoppingCartGateway {

    private final ShoppingCartDataJpaRepository shoppingCartDataJpaRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCart saveCart(ShoppingCart shoppingCart) {
        ShoppingCartData savedCart = shoppingCartDataJpaRepository.save(
                shoppingCartMapper.toData(shoppingCart)
        );
        return shoppingCartMapper.toShoppingCart(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart findCartById(Long cartId) {
        return shoppingCartDataJpaRepository.findById(cartId)
                .map(shoppingCartMapper::toShoppingCart)
                .orElseThrow(CartNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart findActiveCartByUserId(String userId) {
        return shoppingCartDataJpaRepository
                .findFirstByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .map(shoppingCartMapper::toShoppingCart)
                .orElse(null);
    }
}
