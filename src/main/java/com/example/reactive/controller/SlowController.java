package com.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlowController {

    @GetMapping("/slow")
    public String hello() throws InterruptedException {
        return getHello() + getUserName();
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
