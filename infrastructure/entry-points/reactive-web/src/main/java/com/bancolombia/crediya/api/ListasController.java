package com.bancolombia.crediya.api;

import com.bancolombia.crediya.model.estado.Estado;
import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.usecase.listarestados.ListarEstadosUseCase;
import com.bancolombia.crediya.usecase.listartiposprestamo.ListarTiposPrestamoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ListasController {

    private final ListarEstadosUseCase listarEstadosUseCase;
    private final ListarTiposPrestamoUseCase listarTiposPrestamoUseCase;

    @GetMapping("/estados")
    public Flux<Estado> listarEstados() {
        return listarEstadosUseCase.listarEstados();
    }

    @GetMapping("/estados/{id}")
    public Mono<Estado> obtenerEstadoPorId(@PathVariable Integer id) {
        return listarEstadosUseCase.obtenerEstadoPorId(id);
    }

    @GetMapping("/tipos-prestamo")
    public Flux<TipoPrestamo> listarTiposPrestamo() {
        return listarTiposPrestamoUseCase.listarTiposPrestamo();
    }

    @GetMapping("/tipos-prestamo/{id}")
    public Mono<TipoPrestamo> obtenerTipoPrestamoPorId(@PathVariable Integer id) {
        return listarTiposPrestamoUseCase.obtenerTipoPrestamoPorId(id);
    }
}