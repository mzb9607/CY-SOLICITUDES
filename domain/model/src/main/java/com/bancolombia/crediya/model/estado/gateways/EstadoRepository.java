package com.bancolombia.crediya.model.estado.gateways;

import com.bancolombia.crediya.model.estado.Estado;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoRepository {
    Mono<Estado> findById(Integer idEstado);
    Flux<Estado> findAll();
}
