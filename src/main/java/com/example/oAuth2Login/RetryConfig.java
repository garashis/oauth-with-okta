package com.example.oAuth2Login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.*;
import org.springframework.web.reactive.function.client.WebClient;

@EnableRetry
@Configuration
public class RetryConfig {

    @Value("${fakestoreapi}")
    private String fakestoreapi;

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
                .exponentialBackoff(1000, 3, 5000)
                .maxAttempts(4)
                //.retryOn(Collections.singletonList(WebClientResponseException.Forbidden.class))
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(fakestoreapi)
                //.defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
