package com.ecommerce.auth.infrastructure.driver_adapters;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataJpaRepository extends JpaRepository<UserData, String> {

    Optional<UserData> findByMail(String mail);

    boolean existsByMail(String mail);
}
