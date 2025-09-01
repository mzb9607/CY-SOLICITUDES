package com.bancolombia.crediya.usecase.registrarsolicitud;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.bancolombia.crediya.model.estado.solicitud.Solicitud;
import com.bancolombia.crediya.model.estado.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;


    public Mono<Solicitud> registrarSolicitud(Solicitud solicitud) {
        return validarSolicitud(solicitud)
                .flatMap(this::validarTipoPrestamoExistente)
                .doOnNext(s -> s.setIdEstado(1))
                .flatMap(solicitudRepository::save);
    }

    public Mono<Solicitud> validarTipoPrestamoExistente(Solicitud solicitud) {
        return tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .hasElement()
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("No se encontró el tipo de préstamo con el ID proporcionado."));
                    }
                    return Mono.just(solicitud);
                });
    }

    public Mono<Solicitud> validarSolicitud(Solicitud solicitud) {
        List<String> validationErrors = new ArrayList<>();
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        // Validaciones de campos obligatorios
        if (Objects.isNull(solicitud.getMonto())) {
            validationErrors.add("monto");
        }
        if (Objects.isNull(solicitud.getPlazo())) {
            validationErrors.add("plazo");
        }
        if (Objects.isNull(solicitud.getEmail())) {
            validationErrors.add("email");
        }
        if (Objects.isNull(solicitud.getIdTipoPrestamo())) {
            validationErrors.add("documento de identidad");
        }
 
        if (!validationErrors.isEmpty()) {
            String errorMessage = "Los siguientes campos son obligatorios: " + String.join(", ", validationErrors) + ".";
            return Mono.error(new IllegalArgumentException(errorMessage));
        }

        // Validaciones de longitud
        if (solicitud.getEmail().length() > 255) {
            return Mono.error(new IllegalArgumentException("El email no debe tener más de 255 caracteres."));
        }

        // Validación de formato de correo electrónico
        if (!Pattern.matches(emailRegex, solicitud.getEmail())) {
            return Mono.error(new IllegalArgumentException("Debe ingresar un correo electrónico válido."));
        }

        // Validación de rangos
        if (solicitud.getMonto() < 0 || solicitud.getMonto() > 90000000) {
            return Mono.error(new IllegalArgumentException("El monto debe estar entre 0 y 90,000,000."));
        }

        if (solicitud.getPlazo() < 1 || solicitud.getPlazo() > 72) {
            return Mono.error(new IllegalArgumentException("El plazo debe estar entre 1 y 72 meses."));
        }

        return Mono.just(solicitud);
    }


}
