package com.bancolombia.crediya.api.dto;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SolicitudCompletaResponse {
    BigInteger idSolicitud;
    String documentoIdentidad;
    String email;
    String nombres;
    String apellidos;
    Double salario;
    String nombreTipoPrestamo;
    Double monto;
    Integer plazo;
    Double tasaInteres;
    Double valorCuota;
    String nombreEstado;
}