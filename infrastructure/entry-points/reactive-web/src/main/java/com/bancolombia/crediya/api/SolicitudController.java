package com.bancolombia.crediya.api;

import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.api.dto.SolicitudRequest;
import com.bancolombia.crediya.api.dto.SolicitudResponse;
import com.bancolombia.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
public class SolicitudController {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudController.class);
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @PostMapping
    @Operation(
            summary = "Registrar una nueva solicitud",
            description = "Crea una nueva solicitud en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud registrada exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SolicitudResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Error de validación",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "text/plain"))
            }
    )
    public Mono<ResponseEntity<SolicitudResponse>> registrarSolicitud(@RequestBody SolicitudRequest request) {
        logger.info("Request para registrar solicitud recibida.");
        return Mono.just(request)
                .map(req -> Solicitud.builder()
                        .idSolicitud(req.getIdSolicitud())
                        .monto(req.getMonto())
                        .plazo(req.getPlazo())
                        .email(req.getEmail())
                        .documentoIdentidad(req.getDocumentoIdentidad())
                        .idEstado(req.getIdEstado())
                        .idTipoPrestamo(req.getIdTipoPrestamo())
                        .build())
                .flatMap(solicitud -> registrarSolicitudUseCase.registrarSolicitud(solicitud))
                .map(savedSolicitud -> {
                    logger.info("Solicitud registrada exitosamente.");
                    return ResponseEntity.ok().body(SolicitudResponse.builder()
                            .idSolicitud(savedSolicitud.getIdSolicitud())
                            .monto(savedSolicitud.getMonto())
                            .plazo(savedSolicitud.getPlazo())
                            .email(savedSolicitud.getEmail())
                            .documentoIdentidad(savedSolicitud.getDocumentoIdentidad())
                            .idEstado(savedSolicitud.getIdEstado())
                            .idTipoPrestamo(savedSolicitud.getIdTipoPrestamo())
                            .build());
                });
                
    }
}
