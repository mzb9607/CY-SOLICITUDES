package com.bancolombia.crediya.api.dto;

import lombok.AllArgsConstructor;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudResponse {
    private BigInteger idSolicitud;
    private Double monto;
    private Integer plazo;
    private String email;
    private String documentoIdentidad;
    private Integer idEstado;
    private Integer idTipoPrestamo;
}