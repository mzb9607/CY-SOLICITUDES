package com.bancolombia.crediya.model.estado.solicitud;
import lombok.Builder;
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
    Integer idSolicitud;
    Double monto;
    Integer plazo;
    String email;
    Integer idEstado;
    Integer idTipoPrestamo;
}
