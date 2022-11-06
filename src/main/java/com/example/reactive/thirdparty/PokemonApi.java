package com.example.reactive.thirdparty;

import com.example.reactive.config.PokemonClientConfig;
import com.example.reactive.model.PokemonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name="pokemon-client", url="${app.pokemon.url}", configuration = PokemonClientConfig.class)
public interface PokemonApi {
    @RequestMapping(method = RequestMethod.GET, value = "/pokemon/{name}/")
    Mono<PokemonResponse> getPokemonDetailByName(@PathVariable String name);
}
