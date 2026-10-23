package com.ecommerce.auth.application;

import com.ecommerce.auth.domain.model.gateway.PasswordEncoderGateway;
import com.ecommerce.auth.domain.model.gateway.UserGateway;
import com.ecommerce.auth.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public UserUseCase userUseCase(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        return new UserUseCase(userGateway, passwordEncoderGateway);
    }
}
