package com.bancolombia.crediya.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TipoPrestamoDto {
    @JsonProperty("idTipoPrestamo")
    Integer idTipoPrestamo;
    @JsonProperty("nombre")
    String nombre;
    @JsonProperty("montoMinimo")
    Double montoMinimo;
    @JsonProperty("montoMaximo")
    Double montoMaximo;
    @JsonProperty("tasaInteres")
    Double tasaInteres;
    @JsonProperty("validacionAutomatica")
    Boolean validacionAutomatica;
}
