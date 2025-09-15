package com.bancolombia.crediya.api.client;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.bancolombia.crediya.api.dto.UsuarioResponse;

@Service
public class UsuarioClient {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioClient.class);
    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder webClientBuilder, @Value("${adapters.usuario-service.url}") String usuarioServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(usuarioServiceUrl).build();
    }

    public Mono<UsuarioResponse> obtenerUsuarioPorDocumento(String documentoIdentidad, String token) {
        return this.webClient.get()
                .uri("/api/v1/usuarios?documento="+documentoIdentidad)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UsuarioResponse.class)
                .onErrorResume(e -> {
                    logger.error("Error al obtener usuario del servicio de usuarios para documento {}: {}", documentoIdentidad, e.getMessage());
                    return Mono.empty();
                });
    }
}