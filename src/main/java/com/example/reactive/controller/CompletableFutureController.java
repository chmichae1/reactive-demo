package com.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CompletableFutureController {

    @GetMapping("/future")
    public String hello() {
        CompletableFuture<String> helloAsync = CompletableFuture.supplyAsync(() -> {
            try {
                return getHello();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        CompletableFuture<String> userNameAsync = CompletableFuture.supplyAsync(() -> {
            try {
                return getUserName();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        CompletableFuture<Void> bothFuture = CompletableFuture.allOf(helloAsync, userNameAsync);
        String hello = helloAsync.join();
        String userName = userNameAsync.join();
        return hello + userName;
    }

    private String getHello() throws InterruptedException {
        Thread.sleep(5000);
        return "Hello ";
    }

    private String getUserName() throws InterruptedException {
        Thread.sleep(5000);
        return "Michael";
    }
}
