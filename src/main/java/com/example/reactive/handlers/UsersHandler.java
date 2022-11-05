package com.example.reactive.handlers;

import com.example.reactive.entity.Users;
import com.example.reactive.model.User;
import com.example.reactive.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class UsersHandler {
    @Autowired
    private UsersService usersService;

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(usersService.getAllUsers(), Users.class);

    }

    public Mono<ServerResponse> getUser(ServerRequest serverRequest) {
        String uuid = serverRequest.pathVariable("uuid");
        return usersService.getUserByUuid(uuid)
                .flatMap(user ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(user))
                )
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                .body(Mono.just("User " + uuid + " is not found!"), String.class)
                );
    }

    public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(
                    user -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(usersService.saveUser(user), Users.class)
        );
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        Mono<Users> userReq = serverRequest.bodyToMono(Users.class);
        return usersService.updateUser(userReq)
                .flatMap(user ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(fromObject(user))
                )
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                            .body(Mono.just("User not found!"), String.class)
                );
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        String uuid = serverRequest.pathVariable("uuid");
        return usersService.deleteUser(uuid)
                .flatMap(user ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(user))
                )
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                .body(Mono.just("User " + uuid + " is not found!"), String.class)
                );
    }
}