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
public class EstadoDto {
    
    @JsonProperty("idEstado")
    private Integer idEstado;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("descripcion")    
    private String descripcion;
}
