package com.bancolombia.crediya.api.adapters;


import com.bancolombia.crediya.api.client.UsuarioApiClient;
import com.bancolombia.crediya.api.dto.UsuarioResponse;
import com.bancolombia.crediya.model.usuarioclient.UsuarioClient;
import com.bancolombia.crediya.model.usuarioclient.gateways.UsuarioClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsuarioClientHttpAdapter implements UsuarioClientRepository {

    private final UsuarioApiClient usuarioApiClient;

    @Override
    public Mono<UsuarioClient> obtenerUsuarioPorDocumento(String documentoIdentidad, String token) {
        return usuarioApiClient.obtenerUsuarioPorDocumento(documentoIdentidad, token)
                .map(this::mapToDomainUsuario)
                .defaultIfEmpty(UsuarioClient.builder().build());
    }

    private UsuarioClient mapToDomainUsuario(UsuarioResponse response) {
        return UsuarioClient.builder()
                .idUsuario(response.getIdUsuario())
                .documentoIdentidad(response.getDocumentoIdentidad())
                .nombres(response.getNombres())
                .apellidos(response.getApellidos())
                .fechaNacimiento(response.getFechaNacimiento())
                .direccion(response.getDireccion())
                .telefono(response.getTelefono())
                .correoElectronico(response.getCorreoElectronico())
                .salarioBase(response.getSalarioBase())
                .idRol(response.getIdRol())
                .build();
    }
}
