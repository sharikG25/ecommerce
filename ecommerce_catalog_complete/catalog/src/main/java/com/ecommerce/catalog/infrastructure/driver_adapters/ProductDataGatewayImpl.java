package com.ecommerce.catalog.infrastructure.driver_adapters;

import com.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.gateway.ProductGateway;
import com.ecommerce.catalog.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDataGatewayImpl implements ProductGateway {

    private final ProductDataJpaRepository productDataJpaRepository;
    private final ProductMapper productMapper;

    @Override
    public Product saveProduct(Product product) {
        return productMapper.toProduct(
                productDataJpaRepository.save(productMapper.toData(product))
        );
    }

    @Override
    public Product findProductById(Long productId) {
        return productDataJpaRepository.findById(productId)
                .map(productMapper::toProduct)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findAllProducts() {
        return productDataJpaRepository.findAll().stream()
                .map(productMapper::toProduct)
                .toList();
    }

    @Override
    public boolean existsProductById(Long productId) {
        return productDataJpaRepository.existsById(productId);
    }

    @Override
    public void deleteProductById(Long productId) {
        productDataJpaRepository.deleteById(productId);
    }
}
