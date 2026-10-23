package com.ecommerce.catalog.domain.model.gateway;

import java.math.BigDecimal;

public interface AuthGateway {

    boolean userExists(String userId);

    BigDecimal getBalance(String userId);

    BigDecimal debitBalance(String userId, BigDecimal amount);

    BigDecimal rechargeBalance(String userId, BigDecimal amount);
}
