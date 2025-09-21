package com.bancolombia.crediya.api;

import com.bancolombia.crediya.api.dto.ActualizarEstadoSolicitud;
import com.bancolombia.crediya.api.dto.SolicitudCompletaResponse;
import com.bancolombia.crediya.api.dto.SolicitudRequest;
import com.bancolombia.crediya.api.dto.SolicitudResponse;
import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.security.TokenProvider;
import com.bancolombia.crediya.usecase.listarsolicitudes.ListarSolicitudesUseCase;
import com.bancolombia.crediya.usecase.orquestadoractualizacionestado.OrquestadorActualizacionEstadoUseCase;
import com.bancolombia.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SolicitudController {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudController.class);
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final OrquestadorActualizacionEstadoUseCase orquestadorActualizacionEstadoUseCase;
    private final TokenProvider tokenProvider;


    @PostMapping("/api/v1/solicitud")
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
                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado, el documento de identidad no coincide"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public Mono<ResponseEntity<SolicitudResponse>> registrarSolicitud(@RequestBody SolicitudRequest request, ServerWebExchange exchange) {
        logger.info("Iniciando proceso de registro de solicitud para el documento: {}", request.getDocumentoIdentidad());

        String token = resolveToken(exchange);
        if (token == null) {
            logger.warn("No se proporcionó token de autorización.");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        try {
            Claims claims = tokenProvider.getClaims(token);
            String documentoFromToken = claims.get("documentoIdentidad", String.class);

            if (documentoFromToken == null || !documentoFromToken.equals(request.getDocumentoIdentidad())) {
                logger.warn("El documento de identidad del token ({}) no coincide con el de la solicitud ({}).", documentoFromToken, request.getDocumentoIdentidad());
                return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
            }
            logger.info("Token validado exitosamente para el documento: {}", documentoFromToken);
        } catch (Exception e) {
            logger.error("Token inválido o error al procesar claims.", e);
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }


        return Mono.just(request)
                .map(req -> {
                    logger.info("Mapeando SolicitudRequest a Solicitud: {}", req);
                    return Solicitud.builder()
                        .idSolicitud(req.getIdSolicitud())
                        .monto(req.getMonto())
                        .plazo(req.getPlazo())
                        .email(req.getEmail())
                        .documentoIdentidad(req.getDocumentoIdentidad())
                        .idEstado(req.getIdEstado())
                        .idTipoPrestamo(req.getIdTipoPrestamo())
                        .build();
                })
                .flatMap(solicitud -> {
                    logger.info("Llamando al caso de uso para registrar la solicitud: {}", solicitud);
                    return registrarSolicitudUseCase.registrarSolicitud(solicitud);
                })
                .map(savedSolicitud -> {
                    logger.info("Solicitud registrada exitosamente con ID: {}", savedSolicitud.getIdSolicitud());
                    return ResponseEntity.ok().body(SolicitudResponse.builder()
                            .idSolicitud(savedSolicitud.getIdSolicitud())
                            .monto(savedSolicitud.getMonto())
                            .plazo(savedSolicitud.getPlazo())
                            .email(savedSolicitud.getEmail())
                            .documentoIdentidad(savedSolicitud.getDocumentoIdentidad())
                            .idEstado(savedSolicitud.getIdEstado())
                            .idTipoPrestamo(savedSolicitud.getIdTipoPrestamo())
                            .build());
                })
                .doOnError(error -> logger.error("Error al registrar la solicitud: {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));

    }

    @PostMapping("/api/v1/actualizar_estado_solicitud")
    public Mono<ResponseEntity<SolicitudResponse>> actualizarEstadoDeSolicitud(@RequestBody ActualizarEstadoSolicitud request) {
        return orquestadorActualizacionEstadoUseCase.actualizarEstadoYNotificar(request.getIdSolicitud(), request.getIdEstado())
                .map(updatedSolicitud -> ResponseEntity.ok().body(SolicitudResponse.builder()
                        .idSolicitud(updatedSolicitud.getIdSolicitud())
                        .monto(updatedSolicitud.getMonto())
                        .plazo(updatedSolicitud.getPlazo())
                        .email(updatedSolicitud.getEmail())
                        .documentoIdentidad(updatedSolicitud.getDocumentoIdentidad())
                        .idEstado(updatedSolicitud.getIdEstado())
                        .idTipoPrestamo(updatedSolicitud.getIdTipoPrestamo())
                        .build()))
                .doOnError(error -> logger.error("Error al actualizar el estado de la solicitud: {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/api/v1/solicitud")
    @Operation(
            summary = "Listar solicitudes pendientes",
            description = "Lista todas las solicitudes con estado pendiente (idEstado = 1) con información completa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitudes listadas exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SolicitudCompletaResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public Mono<ResponseEntity<Map<String, Object>>> listarSolicitudesPendientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            ServerWebExchange exchange) {
        logger.info("Iniciando proceso de listado de solicitudes pendientes con paginación. Página: {}, Tamaño: {}", page, size);

        String token = resolveToken(exchange);
        if (token == null) {
            logger.warn("No se proporcionó token de autorización para listar solicitudes.");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        try {
            tokenProvider.getClaims(token); // Validar token
            logger.info("Token para listar solicitudes validado exitosamente.");
        } catch (Exception e) {
            logger.error("Token inválido para listar solicitudes.", e);
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        logger.info("Llamando a listarSolicitudesPendientesCompletas con page: {}, size: {}", page, size);
        return listarSolicitudesUseCase.listarSolicitudesPendientesCompletas(page, size, token)
                .collectList()
                .doOnNext(solicitudesList -> logger.info("Se encontraron {} solicitudes pendientes.", solicitudesList.size()))
                .map(solicitudesList -> {
                    int totalElements = solicitudesList.size();
                    int totalPages = (int) Math.ceil((double) totalElements / size);
                    logger.info("Mapeando respuesta: totalElements={}, totalPages={}, currentPage={}", totalElements, totalPages, page);
                    return ResponseEntity.ok().body(Map.of(
                        "content", solicitudesList,
                        "totalElements", totalElements,
                        "totalPages", totalPages,
                        "currentPage", page
                    ));
                })
                .doOnError(error -> logger.error("Error al listar solicitudes pendientes: {}", error.getMessage(), error))
                .onErrorResume(error -> {
                    logger.error("Error inesperado al listar solicitudes pendientes, devolviendo 500.", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor: " + error.getMessage())));
                });
    }

    private String resolveToken(ServerWebExchange exchange) {
        logger.debug("Resolviendo token de autorización.");
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token Bearer encontrado.");
            return bearerToken.substring(7);
        }
        logger.warn("No se encontró token Bearer en la cabecera de autorización.");
        return null;
    }
}
