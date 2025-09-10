package com.bancolombia.crediya.usecase.registrarsolicitud;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @InjectMocks
    private RegistrarSolicitudUseCase registrarSolicitudUseCase;

    private Solicitud solicitud;
    private TipoPrestamo tipoPrestamo;

    @BeforeEach
    void setUp() {
        solicitud = Solicitud.builder()
                .monto(1000000.0)
                .plazo(12)
                .email("test@example.com")
                .idTipoPrestamo(1)
                .build();

        tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("Libre Inversión")
                .build();
    }

    @Test
    void registrarSolicitudExitoso() {
        when(tipoPrestamoRepository.findById(any(Integer.class))).thenReturn(Mono.just(tipoPrestamo));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectNextMatches(s -> s.getIdEstado() == 1 && s.equals(solicitud))
                .verifyComplete();
    }

    @Test
    void registrarSolicitudMontoFaltante() {
        solicitud.setMonto(null);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Los siguientes campos son obligatorios: monto"))
                .verify();
    }

    @Test
    void registrarSolicitudPlazoFaltante() {
        solicitud.setPlazo(null);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Los siguientes campos son obligatorios: plazo"))
                .verify();
    }

    @Test
    void registrarSolicitudEmailFaltante() {
        solicitud.setEmail(null);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Los siguientes campos son obligatorios: email"))
                .verify();
    }

    @Test
    void registrarSolicitudIdTipoPrestamoFaltante() {
        solicitud.setIdTipoPrestamo(null);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Los siguientes campos son obligatorios: idTipoPrestamo"))
                .verify();
    }

    @Test
    void registrarSolicitudEmailExcedeLongitud() {
        solicitud.setEmail("a".repeat(256) + "@example.com");
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El email no debe tener más de 255 caracteres."))
                .verify();
    }

    @Test
    void registrarSolicitudDocumentoIdentidadExcedeLongitud() {
        solicitud.setDocumentoIdentidad("1".repeat(21));
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El documento de identidad no debe tener más de 20 caracteres."))
                .verify();
    }

    @Test
    void registrarSolicitudEmailInvalido() {
        solicitud.setEmail("correo-invalido");
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Debe ingresar un correo electrónico válido."))
                .verify();
    }

    @Test
    void registrarSolicitudMontoFueraDeRango() {
        solicitud.setMonto(90000001.0);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El monto debe estar entre 0 y 90,000,000."))
                .verify();
    }

    @Test
    void registrarSolicitudPlazoFueraDeRango() {
        solicitud.setPlazo(73);
        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El plazo debe estar entre 1 y 72 meses."))
                .verify();
    }

    @Test
    void registrarSolicitudTipoPrestamoNoExistente() {
        when(tipoPrestamoRepository.findById(any(Integer.class))).thenReturn(Mono.empty());

        StepVerifier.create(registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("No se encontró el tipo de préstamo con el ID proporcionado."))
                .verify();
    }
}
