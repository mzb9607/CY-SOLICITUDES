package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.r2dbc.data.TipoPrestamoData;
import com.bancolombia.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;

import reactor.core.publisher.Mono;
import java.math.BigInteger;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TipoPrestamoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    TipoPrestamo,
    TipoPrestamoData,
    BigInteger, 
    TipoPrestamoReactiveRepository
> implements TipoPrestamoRepository
{
    public TipoPrestamoReactiveRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> {
        return mapper.map(d, TipoPrestamo.class);
        });
    }

    public Mono<TipoPrestamo> findById(Integer id) {
        return repository.findById(BigInteger.valueOf(id)).map(this::toEntity);
    }
}
