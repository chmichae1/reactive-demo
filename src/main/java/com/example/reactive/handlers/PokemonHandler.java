package com.example.reactive.handlers;

import com.example.reactive.model.PokemonResponse;
import com.example.reactive.thirdparty.PokemonApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class PokemonHandler {

    @Autowired
    private PokemonApi pokemonApi;


    public Mono<ServerResponse> getPokemonDetailByName(ServerRequest serverRequest){
        return pokemonApi.getPokemonDetailByName(serverRequest.pathVariable("name"))
                .flatMap(pokemonResponse -> {
                    return ServerResponse.ok().body(fromObject(pokemonResponse));
                });
    }
}
