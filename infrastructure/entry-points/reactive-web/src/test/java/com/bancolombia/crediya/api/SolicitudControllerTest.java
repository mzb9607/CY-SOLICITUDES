package com.bancolombia.crediya.api;

import com.bancolombia.crediya.api.dto.SolicitudRequest;
import com.bancolombia.crediya.api.dto.SolicitudResponse;
import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigInteger;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SolicitudControllerTest {

    @Mock
    private RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @InjectMocks
    private SolicitudController solicitudController;

    private SolicitudRequest solicitudRequest;
    private Solicitud solicitud;
    private Solicitud savedSolicitud;

    @BeforeEach
    void setUp() {
        solicitudRequest = SolicitudRequest.builder()
                .idSolicitud(BigInteger.valueOf(123))
                .monto(1000.0)
                .plazo(12)
                .email("test@example.com")
                .idEstado(2)
                .idTipoPrestamo(1)
                .build();

        solicitud = Solicitud.builder()
                .idSolicitud(BigInteger.valueOf(123))
                .monto(1000.0)
                .plazo(12)
                .email("test@example.com")
                .idEstado(2)
                .idTipoPrestamo(1)
                .build();

        savedSolicitud = Solicitud.builder()
                .idSolicitud(BigInteger.valueOf(123))
                .monto(1000.0)
                .plazo(12)
                .email("test@example.com")
                .idEstado(3) // Simulate a change in status after saving
                .idTipoPrestamo(1)
                .build();
    }

    @Test
    void registrarSolicitud_success() {
        when(registrarSolicitudUseCase.registrarSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(savedSolicitud));

        Mono<ResponseEntity<SolicitudResponse>> responseMono = solicitudController.registrarSolicitud(solicitudRequest);

        StepVerifier.create(responseMono)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertEquals(savedSolicitud.getIdSolicitud(), responseEntity.getBody().getIdSolicitud());
                    assertEquals(savedSolicitud.getMonto(), responseEntity.getBody().getMonto());
                    assertEquals(savedSolicitud.getPlazo(), responseEntity.getBody().getPlazo());
                    assertEquals(savedSolicitud.getEmail(), responseEntity.getBody().getEmail());
                    assertEquals(savedSolicitud.getIdEstado(), responseEntity.getBody().getIdEstado());
                    assertEquals(savedSolicitud.getIdTipoPrestamo(), responseEntity.getBody().getIdTipoPrestamo());
                })
                .verifyComplete();
    }

    @Test
    void registrarSolicitud_error() {
        when(registrarSolicitudUseCase.registrarSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al registrar solicitud")));

        Mono<ResponseEntity<SolicitudResponse>> responseMono = solicitudController.registrarSolicitud(solicitudRequest);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error al registrar solicitud"))
                .verify();
    }
}
