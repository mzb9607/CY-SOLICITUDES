package com.bancolombia.crediya.applications.appservice.config;

import com.bancolombia.crediya.usecase.listarestados.ListarEstadosUseCase;
import com.bancolombia.crediya.usecase.listartiposprestamo.ListarTiposPrestamoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheInitializer {

    private final ListarEstadosUseCase listarEstadosUseCase;
    private final ListarTiposPrestamoUseCase listarTiposPrestamoUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        listarEstadosUseCase.listarEstados().subscribe();
        listarTiposPrestamoUseCase.listarTiposPrestamo().subscribe();
    }
}
