package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.r2dbc.data.SolicitudData;
import com.bancolombia.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;

import java.math.BigInteger;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

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

}
