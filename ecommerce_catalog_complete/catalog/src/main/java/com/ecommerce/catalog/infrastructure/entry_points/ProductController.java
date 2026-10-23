package com.ecommerce.catalog.infrastructure.entry_points;

import com.ecommerce.catalog.domain.model.Product;
import com.ecommerce.catalog.domain.usecase.ProductUseCase;
import com.ecommerce.catalog.infrastructure.entry_points.dto.CreateProductRequest;
import com.ecommerce.catalog.infrastructure.entry_points.dto.ProductResponse;
import com.ecommerce.catalog.infrastructure.entry_points.dto.UpdateProductRequest;
import com.ecommerce.catalog.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        Product createdProduct = productUseCase.createProduct(productMapper.toProduct(request));
        return new ResponseEntity<>(productMapper.toResponse(createdProduct), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(
                productMapper.toResponse(productUseCase.findProductById(productId))
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        List<ProductResponse> products = productUseCase.findAllProducts().stream()
                .map(productMapper::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest request
    ) {
        Product updatedProduct = productUseCase.updateProduct(
                productId,
                productMapper.toProduct(request)
        );
        return ResponseEntity.ok(productMapper.toResponse(updatedProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productUseCase.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
