
package com.bancolombia.crediya.api.dto;

import lombok.AllArgsConstructor;
import java.math.BigInteger;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private BigInteger idUsuario;
    private String documentoIdentidad;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Double salarioBase;
    private Integer idRol;
}
