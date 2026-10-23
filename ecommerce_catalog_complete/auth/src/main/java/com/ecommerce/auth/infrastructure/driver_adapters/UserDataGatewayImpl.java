package com.ecommerce.auth.infrastructure.driver_adapters;

import com.ecommerce.auth.domain.exception.UserNotFoundException;
import com.ecommerce.auth.domain.model.User;
import com.ecommerce.auth.domain.model.gateway.UserGateway;
import com.ecommerce.auth.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDataGatewayImpl implements UserGateway {

    private final UserDataJpaRepository userDataJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User saveUser(User user) {
        UserData mappedUser = userMapper.toUserData(user);
        return userMapper.toUser(userDataJpaRepository.save(mappedUser));
    }

    @Override
    public User findUserById(String idUser) {
        return userDataJpaRepository.findById(idUser)
                .map(userMapper::toUser)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findUserByMail(String mail) {
        return userDataJpaRepository.findByMail(mail)
                .map(userMapper::toUser)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean existsByMail(String mail) {
        return userDataJpaRepository.existsByMail(mail);
    }

    @Override
    public User updateUser(User user) {
        UserData mappedUser = userMapper.toUserData(user);
        return userMapper.toUser(userDataJpaRepository.save(mappedUser));
    }

    @Override
    public void deleteUserById(String idUser) {
        userDataJpaRepository.deleteById(idUser);
    }
}
