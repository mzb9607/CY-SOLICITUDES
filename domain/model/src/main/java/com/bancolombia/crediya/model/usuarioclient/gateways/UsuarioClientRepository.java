package com.bancolombia.crediya.model.usuarioclient.gateways;

import com.bancolombia.crediya.model.usuarioclient.UsuarioClient;
import reactor.core.publisher.Mono;

public interface UsuarioClientRepository {
    Mono<UsuarioClient> obtenerUsuarioPorDocumento(String documentoIdentidad, String token);
}
