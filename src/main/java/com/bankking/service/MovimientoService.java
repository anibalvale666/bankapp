package com.bankking.service;


import com.bankking.models.Movimiento;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MovimientoService {
    public List<Movimiento> getMovimientos();

    public Mono<Movimiento> saveMovimiento(Movimiento movimiento) throws Exception;

    public Movimiento updateMovimiento(Long id, Movimiento cuenta);

    public Boolean deleteMovimiento(Long id);
}
