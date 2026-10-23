package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.exception.InsufficientBalanceException;
import com.ecommerce.auth.domain.exception.InvalidBalanceAmountException;
import com.ecommerce.auth.domain.exception.InvalidCredentialsException;
import com.ecommerce.auth.domain.exception.InvalidUserDataException;
import com.ecommerce.auth.domain.exception.UserAlreadyExistsException;
import com.ecommerce.auth.domain.exception.UserNotFoundException;
import com.ecommerce.auth.domain.model.User;
import com.ecommerce.auth.domain.model.gateway.PasswordEncoderGateway;
import com.ecommerce.auth.domain.model.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class UserUseCase {

    private static final BigDecimal ZERO_BALANCE = new BigDecimal("0.00");

    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public User saveUser(User user) {
        validateRequiredCredentials(user.getMail(), user.getPassword());
        validateAge(user.getAge());

        String normalizedMail = normalizeMail(user.getMail());
        if (userGateway.existsByMail(normalizedMail)) {
            throw new UserAlreadyExistsException();
        }

        user.setMail(normalizedMail);
        user.setPassword(passwordEncoderGateway.encode(user.getPassword()));
        user.setBalance(ZERO_BALANCE);
        return userGateway.saveUser(user);
    }

    public User findUserById(String idUser) {
        if (idUser == null || idUser.isBlank()) {
            throw new InvalidUserDataException("El identificador del usuario es obligatorio.");
        }
        return userGateway.findUserById(idUser);
    }

    public User updateUser(String idUser, User changes) {
        User existingUser = findUserById(idUser);

        if (changes.getName() != null) {
            existingUser.setName(changes.getName());
        }

        if (changes.getMail() != null) {
            if (changes.getMail().isBlank()) {
                throw new InvalidUserDataException("El correo electrónico no puede estar vacío.");
            }
            String normalizedMail = normalizeMail(changes.getMail());
            if (!normalizedMail.equals(existingUser.getMail()) && userGateway.existsByMail(normalizedMail)) {
                throw new UserAlreadyExistsException();
            }
            existingUser.setMail(normalizedMail);
        }

        if (changes.getPassword() != null) {
            if (changes.getPassword().isBlank()) {
                throw new InvalidUserDataException("La contraseña no puede estar vacía.");
            }
            existingUser.setPassword(passwordEncoderGateway.encode(changes.getPassword()));
        }

        if (changes.getRole() != null) {
            existingUser.setRole(changes.getRole());
        }

        if (changes.getAge() != null) {
            validateAge(changes.getAge());
            existingUser.setAge(changes.getAge());
        }

        if (changes.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(changes.getPhoneNumber());
        }

        return userGateway.updateUser(existingUser);
    }

    public void deleteUserById(String idUser) {
        findUserById(idUser);
        userGateway.deleteUserById(idUser);
    }

    public User login(String mail, String password) {
        validateRequiredCredentials(mail, password);

        User user;
        try {
            user = userGateway.findUserByMail(normalizeMail(mail));
        } catch (UserNotFoundException exception) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoderGateway.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public BigDecimal getBalance(String idUser) {
        return currentBalance(findUserById(idUser));
    }

    public BigDecimal rechargeBalance(String idUser, BigDecimal amount) {
        BigDecimal validAmount = validateBalanceAmount(amount);
        User user = findUserById(idUser);
        user.setBalance(currentBalance(user).add(validAmount));
        return currentBalance(userGateway.updateUser(user));
    }

    public BigDecimal debitBalance(String idUser, BigDecimal amount) {
        BigDecimal validAmount = validateBalanceAmount(amount);
        User user = findUserById(idUser);
        BigDecimal currentBalance = currentBalance(user);

        if (currentBalance.compareTo(validAmount) < 0) {
            throw new InsufficientBalanceException();
        }

        user.setBalance(currentBalance.subtract(validAmount));
        return currentBalance(userGateway.updateUser(user));
    }

    private void validateRequiredCredentials(String mail, String password) {
        if (mail == null || mail.isBlank()) {
            throw new InvalidUserDataException("El correo electrónico es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidUserDataException("La contraseña es obligatoria.");
        }
    }

    private void validateAge(Integer age) {
        if (age != null && age < 18) {
            throw new InvalidUserDataException("El usuario debe ser mayor de edad.");
        }
    }

    private BigDecimal validateBalanceAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBalanceAmountException();
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal currentBalance(User user) {
        BigDecimal balance = user.getBalance() == null ? ZERO_BALANCE : user.getBalance();
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    private String normalizeMail(String mail) {
        return mail.trim().toLowerCase();
    }
}
