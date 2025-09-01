package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.r2dbc.data.EstadoData;

import java.math.BigInteger;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EstadoReactiveRepository extends ReactiveCrudRepository<EstadoData, BigInteger>, ReactiveQueryByExampleExecutor<EstadoData> {
    
}