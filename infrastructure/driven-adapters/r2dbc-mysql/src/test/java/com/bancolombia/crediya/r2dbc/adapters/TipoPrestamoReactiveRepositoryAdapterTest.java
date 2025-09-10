package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.r2dbc.data.TipoPrestamoData;
import com.bancolombia.crediya.r2dbc.adapters.TipoPrestamoReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoPrestamoReactiveRepositoryAdapterTest {

    @Mock
    private TipoPrestamoReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private TipoPrestamoReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TipoPrestamoReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void testAdapterInstantiation() {
        assertNotNull(adapter);
    }

    @Test
    void testFindByIdInteger() {
        TipoPrestamoData tipoPrestamoData = new TipoPrestamoData();
        tipoPrestamoData.setIdTipoPrestamo(1);
        tipoPrestamoData.setNombre("Hipotecario");

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("Hipotecario")
                .build();

        when(repository.findById(1)).thenReturn(Mono.just(tipoPrestamoData));
        when(mapper.map(any(TipoPrestamoData.class), any())).thenReturn(tipoPrestamo);

        Mono<TipoPrestamo> result = adapter.findById(1);

        StepVerifier.create(result)
                .expectNext(tipoPrestamo)
                .verifyComplete();
    }

    @Test
    void testSave() {
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("Hipotecario")
                .build();

        TipoPrestamoData tipoPrestamoData = new TipoPrestamoData();
        tipoPrestamoData.setIdTipoPrestamo(1);
        tipoPrestamoData.setNombre("Hipotecario");

        when(mapper.map(any(TipoPrestamo.class), any())).thenReturn(tipoPrestamoData);
        when(repository.save(any(TipoPrestamoData.class))).thenReturn(Mono.just(tipoPrestamoData));
        when(mapper.map(any(TipoPrestamoData.class), any())).thenReturn(tipoPrestamo);

        Mono<TipoPrestamo> result = adapter.save(tipoPrestamo);

        StepVerifier.create(result)
                .expectNext(tipoPrestamo)
                .verifyComplete();
    }
}
