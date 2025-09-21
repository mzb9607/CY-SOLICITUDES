package com.bancolombia.crediya.usecase.actualizarestadodesolicitud;

import com.bancolombia.crediya.model.notificacionrevisiondeestado.NotificacionRevisionDeEstado;
import com.bancolombia.crediya.model.notificacionrevisiondeestado.gateways.NotificacionRevisionDeEstadoRepository;
import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.model.estado.gateways.EstadoRedisRepository;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class ActualizarEstadoDeSolicitudUseCase {
    private final SolicitudRepository solicitudRepository;
    private final NotificacionRevisionDeEstadoRepository notificacionRevisionDeEstadoRepository;
    private final EstadoRedisRepository estadoRedisRepository;

    public Mono<Solicitud> actualizarEstadoDeSolicitud(BigInteger idSolicitud, Integer idNuevoEstado) {
        return solicitudRepository.findById(idSolicitud)
                .flatMap(solicitud -> {
                    solicitud.setIdEstado(idNuevoEstado);
                    return solicitudRepository.save(solicitud);
                })
                .flatMap(solicitudActualizada ->
                        estadoRedisRepository.findEstadoById(solicitudActualizada.getIdEstado()) // Retorna Mono<Estado>
                                .flatMap(estado -> // Desempaquetamos el Mono<Estado> para obtener el objeto Estado
                                        notificacionRevisionDeEstadoRepository.enviarNotificacion(
                                                NotificacionRevisionDeEstado.builder()
                                                        .idSolicitud(solicitudActualizada.getIdSolicitud())
                                                        .email(solicitudActualizada.getEmail())
                                                        .estado(estado.getNombre()) // Ahora podemos acceder a getNombre()
                                                        .build()
                                        ).thenReturn(solicitudActualizada)
                                )
                );
    }

}
