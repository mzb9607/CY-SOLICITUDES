package com.bancolombia.crediya.r2dbc.data;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class SolicitudData {

    @Id
    @Column("id_solicitud")
    Integer idSolicitud;

    @Column("monto")
    Double monto;

    @Column("plazo")
    Integer plazo;

    @Column("email")
    String email;

    @Column("documento_identidad")
    String documentoIdentidad;

    @ManyToOne
    @Column("id_estado")
    Integer idEstado;

    @ManyToOne
    @Column("id_tipo_prestamo")
    Integer idTipoPrestamo;
    
}
