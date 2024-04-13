package com.bankking.models.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimientosReporte {
    Date fecha;
    String nombreCliente;
    Integer numeroCuenta;
    String tipoCuenta;
    Double saldoInicial;
    Boolean estado;
    Double valor;
    Double saldoDisponible;
}
