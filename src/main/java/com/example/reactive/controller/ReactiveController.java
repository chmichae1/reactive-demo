package com.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ReactiveController {

    @GetMapping("/reactive")
    public Mono<String> hello() {
        return getHello()
                .zipWith(getUesrName())
                .map(value -> {
                    return value.getT1() + value.getT2();
                });
    }

    private Mono<String> getHello() {
        return Mono.just("Hello ").delayElement(Duration.ofSeconds(5));
    }

    private Mono<String> getUesrName() {
        return Mono.just("Michael").delayElement(Duration.ofSeconds(5));
    }

}
