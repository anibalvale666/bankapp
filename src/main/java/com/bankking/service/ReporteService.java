package com.bankking.service;

import com.bankking.models.response.Reporte;
import reactor.core.publisher.Mono;


public interface ReporteService {
    Mono<Reporte> calcularReporte(String fechaInicio, String fechaFin, Long clienteId);
}
