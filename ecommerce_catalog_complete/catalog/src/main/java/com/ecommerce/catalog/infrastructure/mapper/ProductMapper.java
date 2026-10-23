package com.ecommerce.catalog.infrastructure.mapper;

import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.infrastructure.driver_adapters.ProductData;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CreateProductRequest;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ProductResponse;
import com.ecommerce.catalog.infrastructure.entry_points.dto.UpdateProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductData data) {
        return new Product(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getPrice(),
                data.getStock(),
                data.getImageUrl()
        );
    }

    public ProductData toData(Product product) {
        return new ProductData(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl()
        );
    }

    public Product toProduct(CreateProductRequest request) {
        return new Product(
                null,
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.imageUrl()
        );
    }

    public Product toProduct(UpdateProductRequest request) {
        return new Product(
                null,
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.imageUrl()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl()
        );
    }
}
