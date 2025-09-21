package com.bancolombia.crediya.applications.appservice.config;

import com.bancolombia.crediya.model.estado.gateways.EstadoRedisRepository;
import com.bancolombia.crediya.model.estado.gateways.EstadoRepository;
import com.bancolombia.crediya.model.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRedisRepository;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.bancolombia.crediya.usecase.listarestados.ListarEstadosUseCase;
import com.bancolombia.crediya.usecase.listartiposprestamo.ListarTiposPrestamoUseCase;
import com.bancolombia.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase(SolicitudRepository solicitudRepository, TipoPrestamoRepository tipoPrestamoRepository) {
        return new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository);
    }

    @Bean
    public ListarEstadosUseCase listarEstadosUseCase(
            EstadoRepository estadoRepository,
            EstadoRedisRepository estadoRedisRepository) {
        return new ListarEstadosUseCase(estadoRepository, estadoRedisRepository);
    }

    @Bean
    public ListarTiposPrestamoUseCase listarTiposPrestamoUseCase(
            TipoPrestamoRepository tipoPrestamoRepository,
            TipoPrestamoRedisRepository tipoPrestamoRedisRepository) {
        return new ListarTiposPrestamoUseCase(tipoPrestamoRepository, tipoPrestamoRedisRepository);
    }
}
