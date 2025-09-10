package com.bancolombia.crediya.r2dbc.adapters;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.r2dbc.data.SolicitudData;
import com.bancolombia.crediya.r2dbc.adapters.SolicitudReactiveRepository;
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
class SolicitudReactiveRepositoryAdapterTest {

    @Mock
    private SolicitudReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private SolicitudReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SolicitudReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void testAdapterInstantiation() {
        assertNotNull(adapter);
    }

    @Test
    void testSave() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(BigInteger.valueOf(123))
                .monto(1000.0)
                .plazo(12)
                .email("test@example.com")
                .idEstado(2)
                .idTipoPrestamo(1)
                .build();

        SolicitudData solicitudData = new SolicitudData();
        solicitudData.setIdSolicitud(123);
        solicitudData.setMonto(1000.0);
        solicitudData.setPlazo(12);
        solicitudData.setEmail("test@example.com");
        solicitudData.setIdEstado(2);
        solicitudData.setIdTipoPrestamo(1);

        when(mapper.map(any(Solicitud.class), any())).thenReturn(solicitudData);
        when(repository.save(any(SolicitudData.class))).thenReturn(Mono.just(solicitudData));
        when(mapper.map(any(SolicitudData.class), any())).thenReturn(solicitud);

        Mono<Solicitud> result = adapter.save(solicitud);

        StepVerifier.create(result)
                .expectNext(solicitud)
                .verifyComplete();
    }
}
