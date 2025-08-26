package com.crediya.solicitudes.model.solicitud.gateways;

import com.crediya.solicitudes.model.solicitud.Solicitud;

import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> guardarSolicitud(Solicitud solicitud);
}
