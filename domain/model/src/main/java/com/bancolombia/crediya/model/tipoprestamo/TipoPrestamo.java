package com.bancolombia.crediya.model.tipoprestamo;
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
public class TipoPrestamo {
    Integer idTipoPrestamo;
    String nombre;
    Double montoMinimo;
    Double montoMaximo;
    Double tasaInteres;
    Boolean validacionAutomatica;

}
