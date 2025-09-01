package com.bancolombia.crediya.applications.appservice.config;

import com.bancolombia.crediya.model.estado.solicitud.gateways.SolicitudRepository;
import com.bancolombia.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.bancolombia.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase(SolicitudRepository solicitudRepository, TipoPrestamoRepository tipoPrestamoRepository) {
        return new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository);
    }
}
