package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.Product;

import java.util.List;

public interface ProductGateway {

    Product saveProduct(Product product);

    Product findProductById(Long productId);

    List<Product> findAllProducts();

    boolean existsProductById(Long productId);

    void deleteProductById(Long productId);
}
