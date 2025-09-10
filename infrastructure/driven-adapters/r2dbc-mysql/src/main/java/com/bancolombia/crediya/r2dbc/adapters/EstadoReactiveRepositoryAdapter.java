package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.estado.Estado;
import com.bancolombia.crediya.r2dbc.data.EstadoData;
import com.bancolombia.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.bancolombia.crediya.model.estado.gateways.EstadoRepository;

import reactor.core.publisher.Mono;
import java.math.BigInteger;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EstadoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Estado,
    EstadoData,
    BigInteger, 
    EstadoReactiveRepository
> implements EstadoRepository
{
    public EstadoReactiveRepositoryAdapter(EstadoReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> {
        return mapper.map(d, Estado.class);
    });
}

}
