package com.bancolombia.crediya.model.tipoprestamo.gateways;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Mono;
import java.util.List;

public interface TipoPrestamoRedisRepository {
    Mono<Void> saveAllTiposPrestamo(List<TipoPrestamo> tiposPrestamo);
    Mono<TipoPrestamo> findTipoPrestamoById(Integer id);
}