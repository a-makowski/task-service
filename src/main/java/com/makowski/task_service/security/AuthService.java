package com.makowski.task_service.security;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Mono<String> validateTokenGetUsername(String token) {
        return webClient.post()
                .uri("/user/validate-token")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class);
    }
}
