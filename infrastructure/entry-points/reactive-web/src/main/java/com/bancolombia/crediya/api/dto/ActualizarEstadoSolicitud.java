package com.bancolombia.crediya.api.dto;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActualizarEstadoSolicitud {
    private BigInteger idSolicitud;
    private Integer idEstado;
}
