package com.crediya.solicitudes.model.tipoprestamo.gateways;

import com.crediya.solicitudes.model.estado.Estado;
import com.crediya.solicitudes.model.tipoprestamo.TipoPrestamo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> guardarTipoPrestamo(TipoPrestamo tipoPrestamo);
    Mono<TipoPrestamo> obtenerTipoPrestamo(Integer id);

    Flux<TipoPrestamo> obtenerTotalidadTipoPrestamos();
}
