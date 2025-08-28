CREATE TABLE ESTADO (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    descripcion VARCHAR(255)
);

CREATE TABLE TIPO_PRESTAMO (
    id_tipo_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    monto_minimo DOUBLE,
    monto_maximo DOUBLE,
    tasa_interes DOUBLE,
    validacion_automatica BOOLEAN
);

CREATE TABLE SOLICITUD (
    id_solicitud BIGINT AUTO_INCREMENT PRIMARY KEY,
    documento_identidad VARCHAR(255),
    monto DOUBLE,
    plazo INT,
    email VARCHAR(255),
    id_estado INT,
    id_tipo_prestamo INT,
    FOREIGN KEY (id_estado) REFERENCES ESTADO(id_estado),
    FOREIGN KEY (id_tipo_prestamo) REFERENCES TIPO_PRESTAMO(id_tipo_prestamo)
);