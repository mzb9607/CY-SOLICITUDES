package com.crediya.solicitudes.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Data
@Table("solicitudes") 
public class SolicitudData {
    @Id
    private BigInteger idSolicitud;

    @Column("documento_identidad")
    private String documentoIdentidad;

    @Column("monto")
    private Double monto;

    @Column("plazo")
    private Integer plazo;

    @Column("email")
    private String email;

    @Column("id_estado")
    private Integer idEstado;

    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
}
