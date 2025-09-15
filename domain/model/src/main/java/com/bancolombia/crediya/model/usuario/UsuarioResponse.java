package com.bancolombia.crediya.model.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioResponse {
    private String nombres;
    private String apellidos;
    private Double salarioBase;
}
