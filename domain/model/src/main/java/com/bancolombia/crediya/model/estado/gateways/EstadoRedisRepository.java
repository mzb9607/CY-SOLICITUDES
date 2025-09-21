package com.bancolombia.crediya.model.estado.gateways;

import com.bancolombia.crediya.model.estado.Estado;
import reactor.core.publisher.Mono;
import java.util.List;

public interface EstadoRedisRepository {
    Mono<Void> saveAllEstados(List<Estado> estados);
    Mono<Estado> findEstadoById(Integer id);
}