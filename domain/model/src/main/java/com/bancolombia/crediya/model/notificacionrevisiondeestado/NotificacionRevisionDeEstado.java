package com.bancolombia.crediya.model.notificacionrevisiondeestado;
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
public class NotificacionRevisionDeEstado {
    private BigInteger idSolicitud;
    private String email;
    private String estado;

}
