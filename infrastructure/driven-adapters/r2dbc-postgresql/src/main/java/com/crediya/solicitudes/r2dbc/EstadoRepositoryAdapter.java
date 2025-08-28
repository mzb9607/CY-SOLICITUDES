package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.model.estado.Estado;
import com.crediya.solicitudes.model.estado.gateways.EstadoRepository;
import com.crediya.solicitudes.r2dbc.entity.EstadoData;
import com.crediya.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EstadoRepositoryAdapter extends ReactiveAdapterOperations<
        Estado,
        EstadoData,
        Integer,
        EstadoReactiveRepository> implements EstadoRepository {

    public EstadoRepositoryAdapter(EstadoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Estado.class));
    }

    @Override
    public Mono<Estado> guardarEstado(Estado estado) {
        return save(estado);
    }

    @Override
    public Mono<Estado> obtenerEstado(Integer id) {
        return findById(id);
    }

    @Override
    public Flux<Estado> obtenerTotalidadEstados() {
        return findAll();
    }
}