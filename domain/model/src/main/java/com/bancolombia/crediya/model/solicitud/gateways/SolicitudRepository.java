package com.bancolombia.crediya.model.solicitud.gateways;

import com.bancolombia.crediya.model.solicitud.Solicitud;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);
    Mono<Solicitud> findById(BigInteger idSolicitud);
    Flux<Solicitud> findByIdEstado(Integer idEstado, int page, int size);
}
