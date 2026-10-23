package com.ecommerce.auth.infrastructure.mapper;

import com.ecommerce.auth.domain.model.User;
import com.ecommerce.auth.infrastructure.driver_adapters.UserData;
import com.ecommerce.auth.infrastructure.entry_points.dto.CreateUserRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.UpdateUserRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserMapper {

    public User toUser(UserData userData) {
        return new User(
                userData.getIdUser(),
                userData.getName(),
                userData.getMail(),
                userData.getPassword(),
                userData.getRole(),
                userData.getAge(),
                userData.getPhoneNumber(),
                userData.getBalance() == null ? new BigDecimal("0.00") : userData.getBalance()
        );
    }

    public UserData toUserData(User user) {
        return new UserData(
                user.getIdUser(),
                user.getName(),
                user.getMail(),
                user.getPassword(),
                user.getRole(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getBalance()
        );
    }

    public User toUser(CreateUserRequest request) {
        return new User(
                null,
                request.name(),
                request.mail(),
                request.password(),
                request.role(),
                request.age(),
                request.phoneNumber(),
                null
        );
    }

    public User toUser(UpdateUserRequest request) {
        return new User(
                null,
                request.name(),
                request.mail(),
                request.password(),
                request.role(),
                request.age(),
                request.phoneNumber(),
                null
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getIdUser(),
                user.getName(),
                user.getMail(),
                user.getRole(),
                user.getAge(),
                user.getPhoneNumber(),
                user.getBalance()
        );
    }
}
