package com.bancolombia.crediya.r2dbc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tipo_prestamo")
public class TipoPrestamoData {

    @Id
    @Column("id_tipo_prestamo")
    Integer idTipoPrestamo;

    @Column("nombre")
    String nombre;

    @Column("monto_minimo")
    Double montoMinimo;

    @Column("monto_maximo")
    Double montoMaximo;

    @Column("tasa_interes")
    Double tasaInteres;

    @Column("validacion_automatica")
    Boolean validacionAutomatica;
    
}
