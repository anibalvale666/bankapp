package com.bankking.controllers;

import com.bankking.models.response.Reporte;
import com.bankking.service.ReporteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    @Test
    @DisplayName("Generar reporte con resultado Exitoso")
    void testGenerarReporteSuccess() {
        // Arrange PATRON TRILPE AAA
        String fechaIni = "2021-01-01";
        String fechaFin = "2021-01-31";
        Long cliente = 1L;
        Reporte reporte = new Reporte();
        when(reporteService.calcularReporte(fechaIni, fechaFin, cliente)).thenReturn(Mono.just(reporte));

        // Act
        Mono<Reporte> result = reporteController.generarReporte(fechaIni, fechaFin, cliente);

        // Assert
        StepVerifier.create(result)
            .expectNext(reporte)
            .verifyComplete();

        verify(reporteService).calcularReporte(fechaIni, fechaFin, cliente);
    }

    @Test
    @DisplayName("Generar reporte con resultado Erroneo")
    void testGenerarReporteFailure() {
        // Arrange
        String fechaIni = "2021-01-01";
        String fechaFin = "2021-01-31";
        Long cliente = 1L;
        when(reporteService.calcularReporte(fechaIni, fechaFin, cliente)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente o cuenta inexistente")));

        // Act
        Mono<Reporte> result = reporteController.generarReporte(fechaIni, fechaFin, cliente);

        // Assert
        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                throwable.getMessage().contains("Cliente o cuenta inexistente"))
            .verify();

        verify(reporteService).calcularReporte(fechaIni, fechaFin, cliente);
    }
}