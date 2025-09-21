package com.bancolombia.crediya.usecase.listarestados;

import com.bancolombia.crediya.model.estado.Estado;
import com.bancolombia.crediya.model.estado.gateways.EstadoRepository;
import com.bancolombia.crediya.model.estado.gateways.EstadoRedisRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListarEstadosUseCase {

    private final EstadoRepository estadoRepository;
    private final EstadoRedisRepository estadoRedisRepository;

    public Flux<Estado> listarEstados() {
        return estadoRepository.findAll()
                    .collectList()
                    .flatMapMany(estados ->
                                estadoRedisRepository.saveAllEstados(estados)
                                        .thenMany(Flux.fromIterable(estados))
                                )
                ;
    }

    public Mono<Estado> obtenerEstadoPorId(Integer id) {
        return estadoRedisRepository.findEstadoById(id)
                .switchIfEmpty(
                        estadoRepository.findById(id)
                                .flatMap(estado ->
                                        // Actualizar caché con todos los estados
                                        estadoRepository.findAll()
                                                .collectList()
                                                .flatMap(estados ->
                                                        estadoRedisRepository.saveAllEstados(estados)
                                                                .thenReturn(estado)
                                                )
                                )
                );
    }
}