package com.example.reactive.handlers;

import com.example.reactive.entity.Users;
import com.example.reactive.model.User;
import com.example.reactive.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
@Slf4j
public class UsersHandler {
    @Autowired
    private UsersService usersService;

    private Mono<ServerResponse> responseNotFound (String uuid) {
        log.error("User Not Found: " + uuid);
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .body(Mono.just("User " + uuid + " is not found!"), String.class);
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(usersService.getAllUsers(), Users.class);

    }

    public Mono<ServerResponse> getUser(ServerRequest serverRequest) {
        String uuid = serverRequest.pathVariable("uuid");
        return usersService.getUserByUuid(uuid)
                .flatMap(user -> {
                            log.info("User Found: " + user.toString());
                            return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(fromObject(user));
                        }
                )
                .switchIfEmpty(responseNotFound(uuid));
    }

    public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
        return usersService.saveUser(serverRequest.bodyToMono(User.class))
                .flatMap(user ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(user))
                )
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                .body(Mono.just("User not added!"), String.class)
                );
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        String uuid = serverRequest.pathVariable("uuid");
        Mono<User> userReq = serverRequest.bodyToMono(User.class);
        return usersService.getUserByUuid(uuid)
                .flatMap(user ->
                        usersService.updateUser(userReq, uuid)
                                .flatMap(userUpdated -> {
                                    return ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromObject(userUpdated));
                                    }
                                ))
                .switchIfEmpty(responseNotFound(uuid));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        String uuid = serverRequest.pathVariable("uuid");
        return usersService.getUserByUuid(uuid)
                .flatMap(user ->
                        usersService.deleteUser(user.getUuid())
                                .then(ServerResponse.status(HttpStatus.OK)
                                        .body(Mono.just("User " + user.getUuid() + " has been deleted!"), String.class))
                )
                .switchIfEmpty(responseNotFound(uuid));
    }
}