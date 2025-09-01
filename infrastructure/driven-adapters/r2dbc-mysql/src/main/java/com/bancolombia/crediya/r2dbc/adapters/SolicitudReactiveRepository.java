package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.r2dbc.data.SolicitudData;

import java.math.BigInteger;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudData, BigInteger>, ReactiveQueryByExampleExecutor<SolicitudData> {

}