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
        return userMono.map(AppUtils::modelToEntity)
                .map(user -> {
                    user.setUuid(UUID.randomUUID().toString());
                    return user;
                })
                .flatMap(usersRepository::save)
                .doOnNext(user -> log.info("User Added: " + user));
    }

    public Mono<Users> updateUser(Mono<User> userMono, String uuid) {
        return userMono.map(AppUtils::modelToEntity)
                .map(user -> {
                    user.setUuid(uuid);
                    log.info("User to be updated: " + user);
                    usersRepository.save(user);
                    log.info("User Updated: " + user);
                    return user;
                });
//        return usersRepository.findUserByUuid(uuid)
//                .flatMap(user -> userMono.map(AppUtils::modelToEntity))
//                .doOnNext(user -> user.setUuid(uuid))
//                .flatMap(usersRepository::save)
//                .onErrorContinue((e, element) -> {
//                    System.out.println(e);
//                });
    }

    public Mono<Users> deleteUser(String uuid) {
        return usersRepository.deleteUserByUuid(uuid);
    }
}
