package com.crediya.solicitudes.config;

import com.crediya.solicitudes.model.estado.gateways.EstadoRepository;
import com.crediya.solicitudes.model.solicitud.gateways.SolicitudRepository;
import com.crediya.solicitudes.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.crediya.solicitudes.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase(SolicitudRepository solicitudRepository,
                                                               EstadoRepository estadoRepository,
                                                               TipoPrestamoRepository tipoPrestamoRepository) {
        return new RegistrarSolicitudUseCase(solicitudRepository, estadoRepository, tipoPrestamoRepository);
    }
}