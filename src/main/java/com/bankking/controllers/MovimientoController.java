package com.bankking.controllers;

import com.bankking.models.Movimiento;
import com.bankking.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class MovimientoController {
    @Autowired
    private MovimientoService service;

    @GetMapping("movimientos")
    public List<Movimiento> getCliente() {
        return service.getMovimientos();
    }

    @PostMapping("movimientos")
    public Mono<Movimiento> saveCliente(@RequestBody Movimiento movimiento) throws Exception {
        return service.saveMovimiento(movimiento);
    }

    @PutMapping("movimientos/{id}")
    public Movimiento updateCliente(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        return service.updateMovimiento(id, movimiento);
    }

    @DeleteMapping("movimientos/{id}")
    public Boolean deleteCliente(@PathVariable Long id) {
        return service.deleteMovimiento(id);
    }
}
