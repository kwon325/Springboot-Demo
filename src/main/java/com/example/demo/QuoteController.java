package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @GetMapping("/api/random")
    public Quote getRandomQuote() {
        Value value = new Value(1L, "Working with Spring Boot is fun!");
        return new Quote("success", value);
    }
}
