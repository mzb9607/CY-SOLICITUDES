package com.bancolombia.crediya.api;

import com.bancolombia.crediya.api.client.UsuarioClient;
import com.bancolombia.crediya.api.dto.SolicitudCompletaResponse;
import com.bancolombia.crediya.api.dto.SolicitudRequest;
import com.bancolombia.crediya.api.dto.SolicitudResponse;
import com.bancolombia.crediya.model.estado.gateways.EstadoRepository;
import com.bancolombia.crediya.model.solicitud.Solicitud;
import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.bancolombia.crediya.api.client.UsuarioClient;
import com.bancolombia.crediya.api.dto.UsuarioResponse;
import com.bancolombia.crediya.security.TokenProvider;
import com.bancolombia.crediya.usecase.listarsolicitudes.ListarSolicitudesUseCase;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SolicitudController {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudController.class);
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final TokenProvider tokenProvider;
    private final EstadoRepository estadoRepository;
    private final UsuarioClient usuarioClient;
    private final TipoPrestamoRepository tipoPrestamoRepository;


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

        return listarSolicitudesUseCase.listarSolicitudesPendientes(page, size)
                .doOnSubscribe(subscription -> logger.info("Suscripción al flujo de solicitudes pendientes."))
                .doOnNext(solicitud -> logger.info("Procesando solicitud pendiente ID: {}", solicitud.getIdSolicitud()))
                .flatMap(solicitud -> {
                    logger.debug("Obteniendo datos adicionales para la solicitud ID: {}", solicitud.getIdSolicitud());
                    Mono<UsuarioResponse> usuarioMono = usuarioClient.obtenerUsuarioPorDocumento(solicitud.getDocumentoIdentidad(), token)
                            .doOnSuccess(user -> logger.info("Usuario obtenido exitosamente para documento: {}", solicitud.getDocumentoIdentidad()))
                            .onErrorResume(err -> {
                                logger.error("Error al obtener usuario para documento {}: {}", solicitud.getDocumentoIdentidad(), err.getMessage());
                                return Mono.just(new UsuarioResponse()); // Retornar un objeto vacío para no interrumpir el flujo
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                logger.warn("Usuario no encontrado para documento {}. Retornando UsuarioResponse vacío.", solicitud.getDocumentoIdentidad());
                                return Mono.just(new UsuarioResponse());
                            }));

                    return usuarioMono.flatMap(usuario -> {
                        logger.debug("Entrando a flatMap de usuarioMono. Usuario: {}", usuario);
                        return buildSolicitudCompleta(solicitud, usuario.getNombres(), usuario.getApellidos(), usuario.getSalarioBase());
                    });
                })
                .collectList() // Collect all SolicitudCompletaResponse into a List
                .map(solicitudesList -> {
                    logger.info("Finalizado el procesamiento de todas las solicitudes pendientes. Total: {}, Página: {}, Tamaño: {}", solicitudesList.size(), page, size);
                    return ResponseEntity.ok().body(Map.of(
                            "content", solicitudesList,
                            "totalElements", solicitudesList.size(),
                            "totalPages", (int) Math.ceil((double) solicitudesList.size() / size),
                            "currentPage", page
                    ));
                })
                .doOnError(error -> logger.error("Error en el flujo de listado de solicitudes: {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    private Mono<SolicitudCompletaResponse> buildSolicitudCompleta(Solicitud solicitud, String nombres, String apellidos, Double salarioBase) {
        logger.debug("Iniciando buildSolicitudCompleta para solicitud ID: {}, nombres: {}, apellidos: {}, salarioBase: {}",
                solicitud.getIdSolicitud(), nombres, apellidos, salarioBase);

        Mono<String> estadoMono = estadoRepository.findById(solicitud.getIdEstado())
                .doOnNext(estado -> logger.debug("Estado encontrado para ID {}: {}", solicitud.getIdEstado(), estado.getNombre()))
                .map(estado -> estado.getNombre())
                .defaultIfEmpty("Desconocido")
                .doOnNext(estadoNombre -> logger.debug("Nombre de estado final para ID {}: {}", solicitud.getIdEstado(), estadoNombre));

        Mono<TipoPrestamo> tipoPrestamoMono = tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .doOnNext(tipo -> logger.debug("TipoPrestamo encontrado para ID {}: {}", solicitud.getIdTipoPrestamo(), tipo.getNombre()))
                .defaultIfEmpty(TipoPrestamo.builder().nombre("Desconocido").tasaInteres(0.0).build())
                .doOnNext(tipo -> logger.debug("TipoPrestamo final para ID {}: {}", solicitud.getIdTipoPrestamo(), tipo.getNombre()));

        return Mono.zip(estadoMono, tipoPrestamoMono)
                .map(tuple -> {
                    String nombreEstado = tuple.getT1();
                    TipoPrestamo tipoPrestamo = tuple.getT2();

                    logger.debug("Valores obtenidos de Mono.zip - nombreEstado: {}, tipoPrestamo: {}", nombreEstado, tipoPrestamo.getNombre());

                    Double tasaInteres = tipoPrestamo.getTasaInteres();
                    Double monto = solicitud.getMonto();
                    Integer plazo = solicitud.getPlazo();
                    Double valorCuota = null;

                    logger.debug("Calculando valorCuota - monto: {}, plazo: {}, tasaInteres: {}", monto, plazo, tasaInteres);

                    if (monto != null && plazo != null && tasaInteres != null && plazo > 0) {
                        double monthlyInterestRate = tasaInteres / 12 / 100; // Assuming annual percentage rate
                        if (monthlyInterestRate > 0) {
                            valorCuota = (monto * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -plazo));
                        } else {
                            valorCuota = monto / plazo; // If interest rate is 0
                        }
                    }
                    logger.debug("Valor de cuota calculado: {}", valorCuota);

                    SolicitudCompletaResponse response = SolicitudCompletaResponse.builder()
                            .idSolicitud(solicitud.getIdSolicitud())
                            .documentoIdentidad(solicitud.getDocumentoIdentidad())
                            .email(solicitud.getEmail())
                            .nombres(nombres)
                            .apellidos(apellidos)
                            .salario(salarioBase)
                            .nombreTipoPrestamo(tipoPrestamo.getNombre())
                            .monto(monto)
                            .plazo(plazo)
                            .tasaInteres(tasaInteres)
                            .valorCuota(valorCuota)
                            .nombreEstado(nombreEstado)
                            .build();
                    logger.debug("SolicitudCompletaResponse construida y lista para ser emitida: {}", response);
                    return response;
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
