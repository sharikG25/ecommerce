package com.ecommerce.catalog.domain.usecase;

import com.ecommerce.catalog.domain.exception.InvalidProductDataException;
import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.model.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductGateway productGateway;

    public Product createProduct(Product product) {
        validatePrice(product.getPrice());
        validateStock(product.getStock());
        product.setPrice(normalizeMoney(product.getPrice()));
        return productGateway.saveProduct(product);
    }

    public Product findProductById(Long productId) {
        return productGateway.findProductById(productId);
    }

    public List<Product> findAllProducts() {
        return productGateway.findAllProducts();
    }

    public Product updateProduct(Long productId, Product changes) {
        Product existingProduct = productGateway.findProductById(productId);

        if (changes.getName() != null) {
            existingProduct.setName(changes.getName());
        }

        if (changes.getDescription() != null) {
            existingProduct.setDescription(changes.getDescription());
        }

        if (changes.getPrice() != null) {
            validatePrice(changes.getPrice());
            existingProduct.setPrice(normalizeMoney(changes.getPrice()));
        }

        if (changes.getStock() != null) {
            validateStock(changes.getStock());
            existingProduct.setStock(changes.getStock());
        }

        if (changes.getImageUrl() != null) {
            existingProduct.setImageUrl(changes.getImageUrl());
        }

        return productGateway.saveProduct(existingProduct);
    }

    public void deleteProduct(Long productId) {
        productGateway.findProductById(productId);
        productGateway.deleteProductById(productId);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductDataException("El precio debe ser mayor que cero.");
        }
    }

    private void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new InvalidProductDataException("El inventario no puede ser negativo.");
        }
    }

    private BigDecimal normalizeMoney(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
