package com.bankking.service.Impl;

import com.bankking.exception.ErrorResponse;
import com.bankking.models.Cliente;
import com.bankking.models.Cuenta;
import com.bankking.models.Movimiento;
import com.bankking.models.response.MovimientosReporte;
import com.bankking.models.response.Reporte;
import com.bankking.repository.ClienteRepository;
import com.bankking.repository.CuentaRepository;
import com.bankking.repository.MovimientoRepository;
import com.bankking.service.ReporteService;
import com.bankking.utils.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.bankking.utils.constant.Constants.*;


@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteServiceImpl.class);

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public Mono<Reporte> calcularReporte(String fechaInicio, String fechaFin, Long clienteId) {

        logger.info("Iniciando generar reporte");

        Reporte reporte = new Reporte();
        List<MovimientosReporte> movimientosReporteList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaIn = sdf.parse(fechaInicio);
            Date fechaFi= sdf.parse(fechaFin);

            if (fechaIn.compareTo(fechaFi) >= 0) {
                return Mono.error(new ErrorResponse(
                    Constants.CODE_MESSAGE_ERROR_FECHAS,
                    Constants.MESSAGE_ERROR_FECHAS));
            }

            // obtenemos el cliente
            Optional<Cliente> cliente = clienteRepository.findByClienteId(clienteId);
            if(cliente.isEmpty()) {
                return Mono.error(new ErrorResponse(
                    Constants.CODE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND,
                    Constants.MESSAGE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND));
            }

            // obtenemos todos los movimientos del cliente y los filtraos por fechas
            List<Movimiento> movimientos = movimientoRepository.findByClienteId(clienteId)
                .stream()
                .filter(movimiento -> movimiento.getFecha().after(fechaIn) && movimiento.getFecha().before(fechaFi))
                .collect(Collectors.toList());
            List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

            Map<Long, List<Movimiento>> movimientosPorCuenta = movimientos.stream()
                .collect(Collectors.groupingBy(Movimiento::getCuentaId));

            movimientosPorCuenta.forEach((cuentaId, listaMovimientos) -> {

                listaMovimientos.sort(Comparator.comparing(Movimiento::getFecha));

                Cuenta cuenta = cuentas.stream()
                    .filter(c -> c.getId() == cuentaId)
                    .findFirst()
                    .orElse(null);

                listaMovimientos.forEach(movimiento -> {
                    MovimientosReporte movimientoReporte = MovimientosReporte.builder()
                        .fecha(movimiento.getFecha())
                        .nombreCliente(cliente.get().getNombre())
                        .numeroCuenta(cuenta.getNumeroCuenta())
                        .tipoCuenta(cuenta.getTipoCuenta())
                        .saldoInicial(cuenta.getSaldoInicial())
                        .estado(cuenta.getEstado())
                        .valor(movimiento.getTipoMovimiento()
                            .equals("debito") ? movimiento.getValor() * (-1): movimiento.getValor())
                        .saldoDisponible(movimiento.getSaldo())
                        .build();

                    movimientosReporteList.add(movimientoReporte);
                });

            });
            reporte.setMovimientos(movimientosReporteList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Terminando generar reporte");
        return Mono.just(reporte);
    }
}
