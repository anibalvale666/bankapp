package com.bankking.controllers;

import com.bankking.models.response.Reporte;
import com.bankking.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@RestController
public class ReporteController {

    @Autowired
    private ReporteService service;

    @GetMapping("/reportes")
    public Mono<Reporte> generarReporte(
        @RequestParam(name = "fechaIni", required = true) String fechaIni,
        @RequestParam(name = "fechaFin", required = true) String fechaFin,
        @RequestParam(name = "cliente", required = true) Long cliente) {

        return service.calcularReporte(fechaIni,fechaFin,cliente);
    }
}
