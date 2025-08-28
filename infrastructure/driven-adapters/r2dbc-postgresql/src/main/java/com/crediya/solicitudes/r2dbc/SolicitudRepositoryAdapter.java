package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.model.solicitud.Solicitud;
import com.crediya.solicitudes.model.solicitud.gateways.SolicitudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    @Override
    public Mono<Solicitud> guardarSolicitud(Solicitud solicitud) {
        // TODO: Implement actual R2DBC logic to save the Solicitud
        return Mono.empty();
    }
}

