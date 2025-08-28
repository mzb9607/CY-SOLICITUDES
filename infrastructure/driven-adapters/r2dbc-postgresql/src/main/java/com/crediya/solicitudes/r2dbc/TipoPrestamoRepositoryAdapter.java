package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.model.tipoprestamo.TipoPrestamo;
import com.crediya.solicitudes.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.crediya.solicitudes.r2dbc.entity.TipoPrestamoData;
import com.crediya.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo,
        TipoPrestamoData,
        Integer,
        TipoPrestamoReactiveRepository> implements TipoPrestamoRepository {

    public TipoPrestamoRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class));
    }

    @Override
    public Mono<TipoPrestamo> guardarTipoPrestamo(TipoPrestamo tipoPrestamo) {
        return save(tipoPrestamo);
    }

    @Override
    public Mono<TipoPrestamo> obtenerTipoPrestamo(Integer id) {
        return findById(id);
    }

    @Override
    public Flux<TipoPrestamo> obtenerTotalidadTipoPrestamos() {
        return findAll();
    }
}