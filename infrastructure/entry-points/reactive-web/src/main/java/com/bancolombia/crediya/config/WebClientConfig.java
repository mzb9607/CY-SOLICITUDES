package com.bancolombia.crediya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // This should be externalized to a properties file
        return builder.baseUrl("http://localhost:8080").build();
    }
}
