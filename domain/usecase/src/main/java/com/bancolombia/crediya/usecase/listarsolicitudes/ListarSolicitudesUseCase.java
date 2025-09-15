package com.bancolombia.crediya.usecase.listarsolicitudes;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {
    private final SolicitudRepository solicitudRepository;

    public Flux<Solicitud> listarSolicitudesPendientes(int page, int size) {
        return solicitudRepository.findByIdEstado(1, page, size);
    }
}
