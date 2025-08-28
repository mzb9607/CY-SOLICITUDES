package com.crediya.solicitudes.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estados")
public class EstadoData {
    @Id
    private Integer idEstado;
    @Column("nombre")
    private String nombre;
    @Column("descripcion")
    private String descripcion;
}
