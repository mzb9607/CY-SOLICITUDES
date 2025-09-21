package com.bancolombia.crediya.usecase.listartiposprestamo;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRedisRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListarTiposPrestamoUseCase {

    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final TipoPrestamoRedisRepository tipoPrestamoRedisRepository;

    public Flux<TipoPrestamo> listarTiposPrestamo() {
        return tipoPrestamoRepository.findAll()
                    .collectList()
                    .flatMapMany(tipos ->
                                tipoPrestamoRedisRepository.saveAllTiposPrestamo(tipos)
                                        .thenMany(Flux.fromIterable(tipos))
                                );
    }

    public Mono<TipoPrestamo> obtenerTipoPrestamoPorId(Integer id) {
        return tipoPrestamoRedisRepository.findTipoPrestamoById(id)
                .switchIfEmpty(
                        tipoPrestamoRepository.findById(id)
                                .flatMap(tipo -> 
                                        // Actualizar caché con todos los tipos
                                        tipoPrestamoRepository.findAll()
                                                .collectList()
                                                .flatMap(tipos -> 
                                                        tipoPrestamoRedisRepository.saveAllTiposPrestamo(tipos)
                                                                .thenReturn(tipo)
                                                )
                                )
                );
    }
}