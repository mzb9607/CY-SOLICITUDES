package com.bancolombia.crediya.usecase.listarsolicitudes.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigInteger;

@Getter
@Builder
public class SolicitudCompleta {
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
