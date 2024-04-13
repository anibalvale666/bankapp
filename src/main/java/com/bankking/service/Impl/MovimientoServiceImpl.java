package com.bankking.service.Impl;

import com.bankking.models.Cliente;
import com.bankking.models.Cuenta;
import com.bankking.models.Movimiento;
import com.bankking.repository.ClienteRepository;
import com.bankking.repository.CuentaRepository;
import com.bankking.repository.MovimientoRepository;
import com.bankking.service.MovimientoService;
import com.bankking.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaRepository cuentaRepository;


    @Override
    public List<Movimiento> getMovimientos() {
        return movimientoRepository.findAll();
    }

    @Override
    public Mono<Movimiento> saveMovimiento(Movimiento movimiento) throws Exception {

        //si es debito alidar cuanto ha retirado durante el dia tope max 1000
        Optional<Cliente> cliente = clienteRepository.findByClienteId(movimiento.getClienteId());
        Optional<Cuenta> cuenta = cuentaRepository.findById(movimiento.getCuentaId());

        if(cliente.isEmpty() || cuenta.isEmpty()) {
            return Mono.error(new Exception(Constants.MESSAGE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND));
        }
        // preguntar si es debito (restar) o credito ( sumar)
        Double saldo = obtenerUltimoSaldo(cuenta.get().getId(), cuenta.get().getSaldoInicial());
        if(movimiento.getTipoMovimiento().equals(Constants.CREDITO)) {
            movimiento.setSaldo(saldo + movimiento.getValor());
        } else {
            // obtener acumulado
            Double accum = obtenerAcumulado(cuenta.get().getClienteId());
            if(saldo < movimiento.getValor()) {
                return Mono.error(new Exception(Constants.MESSAGE_ERROR_UNAVAILABLE_BALANCE));
            }
            if(accum + movimiento.getValor() > 1000.0) {
                return Mono.error(new Exception(Constants.MESSAGE_ERROR_DAILY_QUOTA_EXCEEDED));
            }
            movimiento.setSaldo(saldo - movimiento.getValor());
        }
        movimiento.setClienteId(cliente.get().getClienteId());
        movimiento.setCuentaId(cuenta.get().getId());
        return Mono.just(movimientoRepository.save(movimiento));
    }

    private Double obtenerAcumulado(Long clienteId) {

        LocalDate fechaActual = LocalDate.now();
        //LocalDate fechaActual = LocalDate.parse("2023-09-26");

        return movimientoRepository.findByClienteId(clienteId)
            .stream()
            .filter(movimiento -> movimiento.getTipoMovimiento().equals(Constants.DEBITO))
            .filter(movimiento -> {
                LocalDate fechaMovimiento = movimiento.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return fechaMovimiento.equals(fechaActual);
            })
            .map(Movimiento::getValor)
            .reduce(0.0, Double::sum);
    }

    private Double calcularSaldo(Long cuentaId, Double saldoInicial) {
        return movimientoRepository.findByCuentaId(cuentaId)
            .stream()
            .map(movimiento -> Double.valueOf(movimiento.getValor()))
            .reduce(saldoInicial, Double::sum);
    }

    private Double obtenerUltimoSaldo(Long cuentaId, Double SaldoInicial) {
        return movimientoRepository.findByCuentaId(cuentaId)
            .stream()
            .max(Comparator.comparing(Movimiento::getFecha))
            .map(ultimoMovimiento -> ultimoMovimiento.getSaldo())
            .orElse(SaldoInicial);
    }


    @Override
    public Movimiento updateMovimiento(Long id, Movimiento movimiento) {
        Optional<Movimiento> movimientoOptional = movimientoRepository.findById(id);
        if (movimientoOptional.isPresent()) {
            Movimiento movimientoUpdate = movimientoOptional.get();
            movimientoUpdate.setFecha(movimiento.getFecha());
            movimientoUpdate.setTipoMovimiento(movimiento.getTipoMovimiento());
            movimientoUpdate.setValor(movimiento.getValor());
            movimientoUpdate.setSaldo(movimiento.getSaldo());
            movimientoUpdate.setClienteId(movimiento.getClienteId());
            return movimientoRepository.save(movimientoUpdate);
        } else {
            return null;
        }
    }

    @Override
    public Boolean deleteMovimiento(Long id) {
        Movimiento toDelete = movimientoRepository.findById(id).get();
        movimientoRepository.delete(toDelete);
        return true;

        ExecutorService executor = Executors.newFixedThreadPool(heavyDocs,4);

        // procesamiento 4 hilos
        // ...
        try {
            Thread.sleep(20000);
        } catch( InterruptedException ie ){
            Thread.currentThread().interrupt();
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            executor.awaitTermination(20000, TimeUnit.MILLISECONDS);
        }


    }


}
