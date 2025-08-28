package com.crediya.solicitudes.usecase.registrarsolicitud;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.crediya.solicitudes.model.estado.gateways.EstadoRepository;
import com.crediya.solicitudes.model.solicitud.Solicitud;
import com.crediya.solicitudes.model.solicitud.gateways.SolicitudRepository;
import com.crediya.solicitudes.model.tipoprestamo.gateways.TipoPrestamoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    private final EstadoRepository estadoRepository;

    private final TipoPrestamoRepository tipoPrestamoRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public Mono<Solicitud> registrarSolicitud(Solicitud solicitud) {
        //log.info("Iniciando registro de solicitud para el cliente con documento: {}", solicitud.getDocumentoIdentidad());
        solicitud.setIdEstado(1); // 1: Pendiente de revisión
        return Mono.just(solicitud)
                .flatMap(this::validarCampos)
                .flatMap(this::validarTipoPrestamo)
                .flatMap(solicitudRepository::guardarSolicitud);
                //.doOnSuccess(s -> log.info("Solicitud registrada con éxito con el id: {}", s.getIdSolicitud()))
                //.doOnError(e -> log.error("Error al registrar la solicitud: {}", e.getMessage()));
    }

    private Mono<Solicitud> validarCampos(Solicitud solicitud) {
        List<String> errores = new ArrayList<>();
        if (esNuloOVacio(solicitud.getDocumentoIdentidad())) {
            errores.add("Documento de identidad");
        }
        if (Objects.isNull(solicitud.getIdTipoPrestamo())) {
            errores.add("Tipo de préstamo");
        }
        if (Objects.isNull(solicitud.getMonto())) {
            errores.add("Monto");
        }
        if (Objects.isNull(solicitud.getPlazo())) {
            errores.add("Plazo");
        }
        if (esNuloOVacio(solicitud.getEmail())) {
            errores.add("Correo electrónico");
        }

        if (!errores.isEmpty()) {
            String camposFaltantes = String.join(", ", errores);
            return Mono.error(new IllegalArgumentException("Los siguientes campos son obligatorios: " + camposFaltantes + "."));
        }

        if (!EMAIL_PATTERN.matcher(solicitud.getEmail()).matches()) {
            return Mono.error(new IllegalArgumentException("El formato del correo electrónico no es válido."));
        }
        return Mono.just(solicitud);
    }

    private Mono<Solicitud> validarTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.obtenerTipoPrestamo(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de préstamo no existe.")))
                .thenReturn(solicitud);
    }

    private boolean esNuloOVacio(String s) { return s == null || s.trim().isEmpty(); }
}
