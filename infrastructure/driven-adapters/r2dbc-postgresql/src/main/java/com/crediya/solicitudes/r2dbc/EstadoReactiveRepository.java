package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.r2dbc.entity.EstadoData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReactiveRepository extends ReactiveCrudRepository<EstadoData, Integer>, ReactiveQueryByExampleExecutor<EstadoData> {
}
