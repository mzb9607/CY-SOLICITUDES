package com.bancolombia.crediya.usecase.orquestadoractualizacionestado;

import com.bancolombia.crediya.model.notificacionrevisiondeestado.NotificacionRevisionDeEstado;
import com.bancolombia.crediya.model.notificacionrevisiondeestado.gateways.NotificacionRevisionDeEstadoRepository;
import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.usecase.actualizarestadodesolicitud.ActualizarEstadoDeSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class OrquestadorActualizacionEstadoUseCase {

    private final SolicitudRepository solicitudRepository;
    private final ActualizarEstadoDeSolicitudUseCase actualizarEstadoDeSolicitudUseCase;

    public Mono<Solicitud> actualizarEstadoYNotificar(BigInteger idSolicitud, Integer idEstado) {
        return solicitudRepository.findById(idSolicitud)
                .flatMap(solicitud -> 
                    actualizarEstadoDeSolicitudUseCase.actualizarEstadoDeSolicitud(solicitud.getIdSolicitud(), idEstado)
                );
    }
}