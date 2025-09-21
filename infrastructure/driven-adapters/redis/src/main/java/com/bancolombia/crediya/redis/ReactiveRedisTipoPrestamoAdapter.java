package com.bancolombia.crediya.redis;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRedisRepository;
import com.bancolombia.crediya.redis.dto.TipoPrestamoDto;
import com.bancolombia.crediya.redis.helper.ReactiveTemplateAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ReactiveRedisTipoPrestamoAdapter extends ReactiveTemplateAdapterOperations<TipoPrestamo, String, TipoPrestamoDto> implements TipoPrestamoRedisRepository {
    public ReactiveRedisTipoPrestamoAdapter(ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {

        super(connectionFactory, mapper, d -> mapper.map(d, TipoPrestamo.class));
    }

    @Override
    public Mono<Void> saveAllTiposPrestamo(List<TipoPrestamo> tiposPrestamo) {
        log.info("Guardando {} tipos de préstamo en Redis", tiposPrestamo.size());
        return Flux.fromIterable(tiposPrestamo)
                .flatMap(tipo -> {
                    String key = "tipo_prestamo:" + tipo.getIdTipoPrestamo();
                    log.info("Guardando tipo de préstamo con llave: {}", key);
                    return this.save(key, tipo);
                })
                .then()
                .doOnSuccess(v -> log.info("Todos los tipos de préstamo guardados exitosamente"));
    }

    @Override
    public Mono<TipoPrestamo> findTipoPrestamoById(Integer id) {
        String key = "tipo_prestamo:" + id;
        log.info("Buscando tipo de préstamo por ID: {} con llave: {}", id, key);
        return this.findById(key)
                .doOnSuccess(tipo -> {
                    if (tipo != null) {
                        log.info("Tipo de préstamo encontrado para ID: {}", id);
                    } else {
                        log.warn("No se encontró tipo de préstamo para ID: {}", id);
                    }
                });
    }
}
