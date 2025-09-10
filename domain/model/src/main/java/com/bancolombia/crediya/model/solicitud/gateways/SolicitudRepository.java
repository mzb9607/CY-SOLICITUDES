package com.bancolombia.crediya.model.solicitud.gateways;

import com.bancolombia.crediya.model.solicitud.Solicitud;

import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);

}
