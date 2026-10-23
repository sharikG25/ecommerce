package com.ecommerce.auth.infrastructure.entry_points;

import com.ecommerce.auth.domain.model.User;
import com.ecommerce.auth.domain.usecase.UserUseCase;
import com.ecommerce.auth.infrastructure.entry_points.dto.BalanceAmountRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.BalanceResponse;
import com.ecommerce.auth.infrastructure.entry_points.dto.CreateUserRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.LoginRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.LoginResponse;
import com.ecommerce.auth.infrastructure.entry_points.dto.UpdateUserRequest;
import com.ecommerce.auth.infrastructure.entry_points.dto.UserResponse;
import com.ecommerce.auth.infrastructure.mapper.UserMapper;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ecommerce/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@RequestBody CreateUserRequest request) {
        User savedUser = userUseCase.saveUser(userMapper.toUser(request));
        return new ResponseEntity<>(userMapper.toResponse(savedUser), HttpStatus.CREATED);
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String idUser) {
        User foundUser = userUseCase.findUserById(idUser);
        return ResponseEntity.ok(userMapper.toResponse(foundUser));
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String idUser) {
        userUseCase.deleteUserById(idUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String idUser,
            @RequestBody UpdateUserRequest request
    ) {
        User updatedUser = userUseCase.updateUser(idUser, userMapper.toUser(request));
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User authenticatedUser = userUseCase.login(request.mail(), request.password());
        LoginResponse response = new LoginResponse(
                "Inicio de sesión exitoso.",
                userMapper.toResponse(authenticatedUser)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idUser}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String idUser) {
        BigDecimal balance = userUseCase.getBalance(idUser);
        return ResponseEntity.ok(new BalanceResponse(idUser, balance));
    }

    @PostMapping("/{idUser}/balance/recharge")
    public ResponseEntity<BalanceResponse> rechargeBalance(
            @PathVariable String idUser,
            @RequestBody BalanceAmountRequest request
    ) {
        BigDecimal balance = userUseCase.rechargeBalance(idUser, request.amount());
        return ResponseEntity.ok(new BalanceResponse(idUser, balance));
    }

    @PostMapping("/{idUser}/balance/debit")
    public ResponseEntity<BalanceResponse> debitBalance(
            @PathVariable String idUser,
            @RequestBody BalanceAmountRequest request
    ) {
        BigDecimal balance = userUseCase.debitBalance(idUser, request.amount());
        return ResponseEntity.ok(new BalanceResponse(idUser, balance));
    }
}
