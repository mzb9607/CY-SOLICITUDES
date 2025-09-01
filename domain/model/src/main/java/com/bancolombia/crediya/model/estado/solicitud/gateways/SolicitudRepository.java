package com.bancolombia.crediya.model.estado.solicitud.gateways;

import com.bancolombia.crediya.model.estado.solicitud.Solicitud;

import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);

}
