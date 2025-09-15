package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.r2dbc.data.TipoPrestamoData;

import java.math.BigInteger;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import org.springframework.data.r2dbc.repository.Query;


public interface TipoPrestamoReactiveRepository extends ReactiveCrudRepository<TipoPrestamoData, Integer>, ReactiveQueryByExampleExecutor<TipoPrestamoData> {
    
    Mono<TipoPrestamoData> findById(Integer id);
} 