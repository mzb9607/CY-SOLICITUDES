package com.bancolombia.crediya.model.solicitud;
import lombok.Builder;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    BigInteger idSolicitud;
    Double monto;
    Integer plazo;
    String email;
    String documentoIdentidad;
    Integer idEstado;
    Integer idTipoPrestamo;
}
