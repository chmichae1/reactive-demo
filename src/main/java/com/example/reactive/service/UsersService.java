package com.example.reactive.service;

import com.example.reactive.entity.Users;
import com.example.reactive.model.User;
import com.example.reactive.repository.UsersRepository;
import com.example.reactive.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public Flux<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Mono<Users> getUserByUuid(String uuid) {
        return usersRepository.findUserByUuid(uuid);
    }

    public Mono<Users> saveUser(User user) {
        Users userSave = new Users();
        userSave = ObjectMapper.map(user, userSave);
        userSave.setUuid(UUID.randomUUID().toString());
        return usersRepository.save(userSave);
    }

    public Mono<Users> updateUser(Mono<Users> userMono) {
        Mono<Users> updateUser = userMono.flatMap(user -> {
            System.out.println("asdasdasdasda "+user);
            Mono<Users> userFind = usersRepository.findUserByUuid(user.getUuid()).doOnNext(System.out::println);
            userFind.flatMap(currentUser -> {
                System.out.println("asdsad "+currentUser);
                currentUser.setUuid(user.getUuid());
                currentUser.setName(user.getName());
                currentUser.setPhone(user.getPhone());
                currentUser.setEmail(user.getEmail());
                return usersRepository.save(currentUser);
            }).doOnNext(System.out::println);
            return userFind;
        }).doOnNext(System.out::println);
        return updateUser;
    }

    public Mono<Users> deleteUser(String uuid) {
        Mono<Users> userFind = usersRepository.findUserByUuid(uuid)
                .doOnNext(users -> {
                    usersRepository.deleteUserByUuid(uuid);
                });
        return userFind;
    }
}
