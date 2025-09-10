package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.r2dbc.data.TipoPrestamoData;
import com.bancolombia.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;

import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TipoPrestamoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    TipoPrestamo,
    TipoPrestamoData,
    Integer, 
    TipoPrestamoReactiveRepository
> implements TipoPrestamoRepository
{
    public TipoPrestamoReactiveRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> {
        return mapper.map(d, TipoPrestamo.class);
        });
    }

    public Mono<TipoPrestamo> findById(Integer id) {
        System.err.println("Holi uwu: " + id);
        return repository.findById(id).map(this::toEntity);
    }
}
