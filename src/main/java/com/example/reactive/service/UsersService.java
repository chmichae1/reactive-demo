package com.example.reactive.service;

import com.example.reactive.entity.Users;
import com.example.reactive.model.User;
import com.example.reactive.repository.UsersRepository;
import com.example.reactive.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public Flux<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Mono<Users> getUserByUuid(String uuid) {
        return usersRepository.findUserByUuid(uuid);
    }

    public Mono<Users> saveUser(Mono<User> userMono) {
        return userMono
                .map(AppUtils::modelToEntity)
                .map(this::constructUser)
                .flatMap(usersRepository::save)
                .doOnNext(user -> log.info("User Added: " + user));
    }

    public Mono<Users> updateUser(Mono<User> userMono, String uuid) {
        return userMono.map(AppUtils::modelToEntity)
                .map(user -> {
                    user.setUuid(uuid);
                    user.setName(user.getName().toUpperCase());
                    return user;
                })
                .flatMap(usersRepository::save)
                .doOnNext(user -> log.info("User Updated: " + user));
    }

    public Mono<Users> deleteUser(String uuid) {
        return usersRepository.deleteUserByUuid(uuid);
    }

    private Users constructUser(Users users) {
        users.setUuid(UUID.randomUUID().toString());
        users.setName(users.getName().toUpperCase());
        users.setAsNew();
        return users;
    }
}
