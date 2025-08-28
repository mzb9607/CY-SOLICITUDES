package com.crediya.solicitudes.model.estado;
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
public class Estado {
    Integer idEstado;
    String nombre;
    String descripcion;
}
