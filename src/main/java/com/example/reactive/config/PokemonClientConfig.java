package com.example.reactive.config;

import com.example.reactive.thirdparty.PokemonApi;
import feign.RetryableException;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.ReactiveOptions;
import reactivefeign.client.ReactiveHttpResponse;
import reactivefeign.client.log.DefaultReactiveLogger;
import reactivefeign.client.log.ReactiveLoggerListener;
import reactivefeign.client.statushandler.ReactiveStatusHandler;
import reactivefeign.client.statushandler.ReactiveStatusHandlers;
import reactivefeign.retry.BasicReactiveRetryPolicy;
import reactivefeign.retry.ReactiveRetryPolicy;
import reactivefeign.webclient.WebReactiveOptions;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.function.BiFunction;

@Configuration
public class PokemonClientConfig {

    @Bean
    public ReactiveLoggerListener loggerListener() {
        return new DefaultReactiveLogger(Clock.systemUTC(), LoggerFactory.getLogger(PokemonApi.class.getName()));
    }

    @Bean
    public ReactiveRetryPolicy reactiveRetryPolicy() {
        return BasicReactiveRetryPolicy.retryWithBackoff(3, 2000);
    }

    @Bean
    public ReactiveStatusHandler reactiveStatusHandler() {
        return ReactiveStatusHandlers.throwOnStatus(
                (status) -> (status == 500),
                errorFunction());
    }

    private BiFunction<String, ReactiveHttpResponse, Throwable> errorFunction() {
        return (methodKey, response) -> {
            return new RetryableException(response.status(), "", null, Date.from(Instant.EPOCH), null);
        };
    }

    @Bean
    public ReactiveOptions reactiveOptions() {
        return new WebReactiveOptions.Builder()
                .setReadTimeoutMillis(2000)
                .setWriteTimeoutMillis(2000)
                .setResponseTimeoutMillis(2000)
                .build();
    }
}
