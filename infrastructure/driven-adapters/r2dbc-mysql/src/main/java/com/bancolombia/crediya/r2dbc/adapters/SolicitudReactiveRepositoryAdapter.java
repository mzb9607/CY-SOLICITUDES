package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.r2dbc.data.SolicitudData;
import com.bancolombia.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;

import java.math.BigInteger;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Solicitud,
    SolicitudData,
    BigInteger, 
    SolicitudReactiveRepository
> implements SolicitudRepository
{
    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> {
        return mapper.map(d, Solicitud.class);
    });
    }

    @Override
    public Flux<Solicitud> findByIdEstado(Integer idEstado, int page, int size) {
        return repository.findByIdEstado(idEstado, PageRequest.of(page, size))
                .map(this::toEntity);
    }
}
