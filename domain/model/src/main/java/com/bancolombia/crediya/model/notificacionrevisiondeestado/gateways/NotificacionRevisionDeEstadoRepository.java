package com.bancolombia.crediya.model.notificacionrevisiondeestado.gateways;

import com.bancolombia.crediya.model.notificacionrevisiondeestado.NotificacionRevisionDeEstado;
import reactor.core.publisher.Mono;

public interface NotificacionRevisionDeEstadoRepository {
    Mono<String> enviarNotificacion(NotificacionRevisionDeEstado notificacion);
}
