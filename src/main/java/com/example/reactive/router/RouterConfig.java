package com.example.reactive.router;

import com.example.reactive.handlers.UsersHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {
    private String USERS_END_POINT = "/api/users";
    @Bean
    public RouterFunction<ServerResponse> usersRoutes(UsersHandler usersHandler) {
        return RouterFunctions.
                route(GET(USERS_END_POINT).and(accept(MediaType.APPLICATION_JSON))
                        , usersHandler::getAllUsers)
                .andRoute(GET(USERS_END_POINT + "/{uuid}").and(accept(MediaType.APPLICATION_JSON))
                        , usersHandler::getUser)
                .andRoute(POST(USERS_END_POINT).and(accept(MediaType.APPLICATION_JSON))
                        , usersHandler::addUser)
                .andRoute(PUT(USERS_END_POINT + "/{uuid}").and(accept(MediaType.APPLICATION_JSON))
                        , usersHandler::updateUser)
                .andRoute(DELETE(USERS_END_POINT + "/{uuid}").and(accept(MediaType.APPLICATION_JSON))
                        , usersHandler::deleteUser)
                ;
    }
}
