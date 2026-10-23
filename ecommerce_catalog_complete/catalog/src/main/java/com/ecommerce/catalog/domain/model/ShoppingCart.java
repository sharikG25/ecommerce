package com.ecommerce.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCart {

    private Long id;
    private String userId;
    private CartStatus status;
    private List<CartItem> items;
    private BigDecimal total;
}
