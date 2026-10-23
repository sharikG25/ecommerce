package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.User;

public interface UserGateway {

    User saveUser(User user);

    User findUserById(String idUser);

    User findUserByMail(String mail);

    boolean existsByMail(String mail);

    User updateUser(User user);

    void deleteUserById(String idUser);
}
