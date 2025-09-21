package com.bancolombia.crediya.redis;

import com.bancolombia.crediya.model.estado.Estado;
import com.bancolombia.crediya.redis.dto.EstadoDto;
import com.bancolombia.crediya.redis.helper.ReactiveTemplateAdapterOperations;
import com.bancolombia.crediya.model.estado.gateways.EstadoRedisRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReactiveRedisEstadoAdapter extends ReactiveTemplateAdapterOperations<Estado, String, EstadoDto> implements EstadoRedisRepository
{
    public ReactiveRedisEstadoAdapter(ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, Estado.class));
    }

    @Override
    public Mono<Void> saveAllEstados(List<Estado> estados) {
        return Flux.fromIterable(estados)
                .flatMap(estado -> {
                    String key = "estado:" + estado.getIdEstado(); 
                    return this.save(key, estado);
                })
                .then();
    }

    @Override
    public Mono<Estado> findEstadoById(Integer id) {
        String key = "estado:" + id;
        return this.findById(key);
    }


}
