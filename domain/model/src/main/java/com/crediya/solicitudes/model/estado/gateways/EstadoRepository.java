package com.crediya.solicitudes.model.estado.gateways;

import com.crediya.solicitudes.model.estado.Estado;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoRepository {
    Mono<Estado> guardarEstado(Estado estado);

    Mono<Estado> obtenerEstado(Integer id);

    Flux<Estado> obtenerTotalidadEstados();
}
