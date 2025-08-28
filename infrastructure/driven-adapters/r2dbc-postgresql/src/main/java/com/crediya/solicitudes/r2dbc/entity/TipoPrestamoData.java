package com.crediya.solicitudes.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tipos_prestamo")
public class TipoPrestamoData {
    @Id
    private Integer idTipoPrestamo;
    @Column("nombre")
    private String nombre;
    @Column("monto_minimo")
    private Double montoMinimo;
    @Column("monto_maximo")
    private Double montoMaximo;
    @Column("tasa_interes")
    private Double tasaInteres;
    @Column("validacion_automatica")
    private Boolean validacionAutomatica;
}
