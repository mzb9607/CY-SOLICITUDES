package com.bancolombia.crediya.usecase.listarsolicitudes;

import com.bancolombia.crediya.model.estado.gateways.EstadoRepository;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.model.tipoprestamo.TipoPrestamo;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.bancolombia.crediya.model.usuarioclient.UsuarioClient;
import com.bancolombia.crediya.model.usuarioclient.gateways.UsuarioClientRepository;
import com.bancolombia.crediya.usecase.listarsolicitudes.dto.SolicitudCompleta;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {
    private static final Logger logger = Logger.getLogger(ListarSolicitudesUseCase.class.getName());

    private final SolicitudRepository solicitudRepository;
    private final UsuarioClientRepository usuarioClientRepository;
    private final EstadoRepository estadoRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Flux<SolicitudCompleta> listarSolicitudesPendientesCompletas(int page, int size, String token) {
        logger.log(Level.INFO, "Iniciando listarSolicitudesPendientesCompletas con page: {0}, size: {1}, token: {2}", new Object[]{page, size, token});
        return solicitudRepository.findByIdEstado(1, page, size)
                .flatMap(solicitud ->
                        usuarioClientRepository.obtenerUsuarioPorDocumento(solicitud.getDocumentoIdentidad(), token)
                        .defaultIfEmpty(UsuarioClient.builder().build())
                        .flatMap(usuario -> 
                            buildSolicitudCompleta(solicitud, usuario.getNombres(), 
                                usuario.getApellidos(), usuario.getSalarioBase())
                        )
                )
                .doOnComplete(() -> logger.log(Level.INFO, "Finalizado listarSolicitudesPendientesCompletas."));
    }

    private Mono<SolicitudCompleta> buildSolicitudCompleta(
        com.bancolombia.crediya.model.solicitud.Solicitud solicitud, 
        String nombres, String apellidos, Double salarioBase) {
        logger.log(Level.INFO, "Iniciando buildSolicitudCompleta para solicitud ID: {0}, nombres: {1}, apellidos: {2}, salarioBase: {3}", 
            new Object[]{solicitud.getIdSolicitud(), nombres, apellidos, salarioBase});
        
        return Mono.zip(
            estadoRepository.findById(solicitud.getIdEstado())
                .map(estado -> estado.getNombre())
                .defaultIfEmpty("Desconocido"),
            tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .defaultIfEmpty(TipoPrestamo.builder().nombre("Desconocido").tasaInteres(0.0).build())
        ).map(tuple -> {
            String nombreEstado = tuple.getT1();
            TipoPrestamo tipoPrestamo = tuple.getT2();
            
            Double valorCuota = calcularValorCuota(
                solicitud.getMonto(), 
                solicitud.getPlazo(), 
                tipoPrestamo.getTasaInteres()
            );

            return SolicitudCompleta.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .documentoIdentidad(solicitud.getDocumentoIdentidad())
                .email(solicitud.getEmail())
                .nombres(nombres)
                .apellidos(apellidos)
                .salario(salarioBase)
                .nombreTipoPrestamo(tipoPrestamo.getNombre())
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .tasaInteres(tipoPrestamo.getTasaInteres())
                .valorCuota(valorCuota)
                .nombreEstado(nombreEstado)
                .build();
        })
        .doOnNext(solicitudCompleta -> logger.log(Level.INFO, "Finalizado buildSolicitudCompleta, SolicitudCompleta: {0}", solicitudCompleta));
    }

    private Double calcularValorCuota(Double monto, Integer plazo, Double tasaInteres) {
        if (monto != null && plazo != null && tasaInteres != null && plazo > 0) {
            double monthlyInterestRate = tasaInteres / 12 / 100;
            if (monthlyInterestRate > 0) {
                return (monto * monthlyInterestRate) / 
                    (1 - Math.pow(1 + monthlyInterestRate, -plazo));
            }
            return monto / plazo;
        }
        return null;
    }
}