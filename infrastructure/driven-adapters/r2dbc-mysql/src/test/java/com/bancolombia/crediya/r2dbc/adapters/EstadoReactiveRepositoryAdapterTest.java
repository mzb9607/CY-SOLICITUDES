package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.estado.Estado;
import com.bancolombia.crediya.r2dbc.data.EstadoData;
import com.bancolombia.crediya.r2dbc.adapters.EstadoReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoReactiveRepositoryAdapterTest {

    @Mock
    private EstadoReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private EstadoReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new EstadoReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void testAdapterInstantiation() {
        assertNotNull(adapter);
    }

    @Test
    void testFindById() {
        BigInteger id = BigInteger.valueOf(1);
        EstadoData estadoData = new EstadoData();
        estadoData.setIdEstado(1);
        estadoData.setDescripcion("Activo");

        Estado estado = Estado.builder()
                .idEstado(1)
                .descripcion("Activo")
                .build();

        when(repository.findById(id)).thenReturn(Mono.just(estadoData));
        when(mapper.map(any(EstadoData.class), any())).thenReturn(estado);

        Mono<Estado> result = adapter.findById(id);

        StepVerifier.create(result)
                .expectNext(estado)
                .verifyComplete();
    }
}
