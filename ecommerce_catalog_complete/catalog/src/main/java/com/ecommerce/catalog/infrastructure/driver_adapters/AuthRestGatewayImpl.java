package com.ecommerce.catalog.infrastructure.driver_adapters;

import com.ecommerce.catalog.domain.exception.AuthServiceException;
import com.ecommerce.catalog.domain.exception.InsufficientBalanceException;
import com.ecommerce.catalog.domain.exception.UserNotFoundException;
import com.ecommerce.catalog.domain.model.gateway.AuthGateway;
import com.ecommerce.catalog.infrastructure.driver_adapters.dto.AuthBalanceRequest;
import com.ecommerce.catalog.infrastructure.driver_adapters.dto.AuthBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AuthRestGatewayImpl implements AuthGateway {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    public boolean userExists(String userId) {
        try {
            restTemplate.getForEntity(
                    authServiceUrl + "/api/ecommerce/users/{userId}",
                    String.class,
                    userId
            );
            return true;
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        } catch (RestClientException exception) {
            throw new AuthServiceException();
        }
    }

    @Override
    public BigDecimal getBalance(String userId) {
        try {
            ResponseEntity<AuthBalanceResponse> response = restTemplate.getForEntity(
                    authServiceUrl + "/api/ecommerce/users/{userId}/balance",
                    AuthBalanceResponse.class,
                    userId
            );
            return requireBalance(response);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UserNotFoundException();
        } catch (RestClientException exception) {
            throw new AuthServiceException();
        }
    }

    @Override
    public BigDecimal debitBalance(String userId, BigDecimal amount) {
        try {
            ResponseEntity<AuthBalanceResponse> response = restTemplate.postForEntity(
                    authServiceUrl + "/api/ecommerce/users/{userId}/balance/debit",
                    new AuthBalanceRequest(amount),
                    AuthBalanceResponse.class,
                    userId
            );
            return requireBalance(response);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UserNotFoundException();
        } catch (HttpClientErrorException.Conflict exception) {
            throw new InsufficientBalanceException();
        } catch (RestClientException exception) {
            throw new AuthServiceException();
        }
    }

    @Override
    public BigDecimal rechargeBalance(String userId, BigDecimal amount) {
        try {
            ResponseEntity<AuthBalanceResponse> response = restTemplate.postForEntity(
                    authServiceUrl + "/api/ecommerce/users/{userId}/balance/recharge",
                    new AuthBalanceRequest(amount),
                    AuthBalanceResponse.class,
                    userId
            );
            return requireBalance(response);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UserNotFoundException();
        } catch (RestClientException exception) {
            throw new AuthServiceException();
        }
    }

    private BigDecimal requireBalance(ResponseEntity<AuthBalanceResponse> response) {
        if (response.getBody() == null || response.getBody().balance() == null) {
            throw new AuthServiceException();
        }
        return response.getBody().balance();
    }
}
