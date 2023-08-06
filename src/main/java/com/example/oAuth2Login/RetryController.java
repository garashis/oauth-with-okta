package com.example.oAuth2Login;

import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class RetryController {
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public RetryController(RetryTemplate retryTemplate, WebClient webClient) {
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    @GetMapping("/retryService")
    public String retryService(OAuth2AuthenticationToken authentication) {
        String response = retryTemplate.execute(arg0 -> {
            System.out.println(">>>>>>>>>> retry");
            return webClient.get()
                    .uri("/users")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    //.body(Mono.just(empl), Employee.class)
                    .retrieve()
                    .bodyToMono(String.class).block();
        });
        return response;
    }

    @Retryable
    @GetMapping("/retryService1")
    public String retryService1(OAuth2AuthenticationToken authentication) {
        System.out.println(">>>>>>>>>> retry");
        return webClient.get()
                .uri("/users")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.body(Mono.just(empl), Employee.class)
                .retrieve()
                .bodyToMono(String.class).block();
    }
}
