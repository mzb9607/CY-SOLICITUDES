package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.r2dbc.entity.TipoPrestamoData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPrestamoReactiveRepository extends ReactiveCrudRepository<TipoPrestamoData, Integer> , ReactiveQueryByExampleExecutor<TipoPrestamoData> {
}
