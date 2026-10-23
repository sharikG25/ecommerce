package com.ecommerce.catalog.application;

import com.ecommerce.catalog.domain.model.gateway.AuthGateway;
import com.ecommerce.catalog.domain.model.gateway.CheckoutGateway;
import com.ecommerce.catalog.domain.model.gateway.ProductGateway;
import com.ecommerce.catalog.domain.model.gateway.ShoppingCartGateway;
import com.ecommerce.catalog.domain.usecase.CheckoutUseCase;
import com.ecommerce.catalog.domain.usecase.ProductUseCase;
import com.ecommerce.catalog.domain.usecase.ShoppingCartUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CatalogConfig {

    @Bean
    public ProductUseCase productUseCase(ProductGateway productGateway) {
        return new ProductUseCase(productGateway);
    }

    @Bean
    public ShoppingCartUseCase shoppingCartUseCase(
            ShoppingCartGateway shoppingCartGateway,
            ProductGateway productGateway,
            AuthGateway authGateway
    ) {
        return new ShoppingCartUseCase(shoppingCartGateway, productGateway, authGateway);
    }

    @Bean
    public CheckoutUseCase checkoutUseCase(
            ShoppingCartGateway shoppingCartGateway,
            ProductGateway productGateway,
            AuthGateway authGateway,
            CheckoutGateway checkoutGateway
    ) {
        return new CheckoutUseCase(shoppingCartGateway, productGateway, authGateway, checkoutGateway);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
