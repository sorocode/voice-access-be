package com.sorocode.voice_access_be_demo.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("📤 [요청] " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value ->
                    System.out.println("📜 " + name + ": " + value)
            ));
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("📥 [응답] Status: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://127.0.0.1:5001") // 백엔드 API URL
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .filter(logRequest())  // 요청 로깅
                .filter(logResponse()) // 응답 로깅
                .build();
    }
}
