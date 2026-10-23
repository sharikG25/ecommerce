package com.ecommerce.auth.infrastructure.driver_adapters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "USUARIOS")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idUser;

    private String name;

    @Column(length = 120, nullable = false, unique = true)
    private String mail;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "rol")
    private String role;

    private Integer age;
    private String phoneNumber;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;
}
