package com.bancolombia.crediya.model.tipoprestamo.gateways;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> findById(Integer id);
    Flux<TipoPrestamo> findAll();

}
