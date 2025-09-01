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
@Table("estado")
public class EstadoData {

    @Id
    @Column("id_estado")
    Integer idEstado;

    @Column("nombre")
    String nombre;

    @Column("descripcion")
    String descripcion;
    
}
