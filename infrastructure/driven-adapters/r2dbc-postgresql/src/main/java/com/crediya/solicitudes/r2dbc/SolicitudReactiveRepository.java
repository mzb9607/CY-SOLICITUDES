package com.crediya.solicitudes.r2dbc;

import com.crediya.solicitudes.r2dbc.entity.SolicitudData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface SolicitudReactiveRepository extends R2dbcRepository<SolicitudData, Long> {

}