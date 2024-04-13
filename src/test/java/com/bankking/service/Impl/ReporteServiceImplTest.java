package com.bankking.service.Impl;

import com.bankking.models.Cliente;
import com.bankking.models.Cuenta;
import com.bankking.models.Movimiento;
import com.bankking.models.response.Reporte;
import com.bankking.repository.ClienteRepository;
import com.bankking.repository.CuentaRepository;
import com.bankking.repository.MovimientoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    ReporteServiceImpl reporteService;

    @Test
    @DisplayName("Execute CalcularReporte And Then Return Success")
    void calcularReporteReturnSuccess() throws ParseException {
        String fechaIni = "01/01/2023";
        String fechaFin = "31/01/2023";
        Long clientId = 12345L;

        Cliente cliente = getCliente();
        Movimiento movimiento = getMovimiento();
        Cuenta cuenta = getCuenta();


        when(clienteRepository.findByClienteId(anyLong()))
            .thenReturn(Optional.of(cliente));

        when(movimientoRepository.findByClienteId(anyLong()))
            .thenReturn(List.of(movimiento));

        when(cuentaRepository.findByClienteId(anyLong()))
            .thenReturn(List.of(cuenta));

        Mono<Reporte> reporteMono = reporteService.calcularReporte("01/01/2023", "31/01/2023", 12345L);

        StepVerifier.create(reporteMono)
            .expectNextMatches(reporte -> reporte.getMovimientos().size() > 0)
            .verifyComplete();
    }

    @Test
    @DisplayName("Execute CalcularReporte And Then Return Error")
    void calcularReporteReturnError() {

        String fechaIni = "01/01/2023";
        String fechaFin = "31/01/2023";
        Long clientId = 12345L;
        when(clienteRepository.findByClienteId(anyLong()))
            .thenReturn(Optional.empty());


        Mono<Reporte> reporteMono = reporteService
            .calcularReporte(fechaIni, fechaFin, clientId);

        StepVerifier.create(reporteMono)
            .expectErrorMatches(
                throwable -> throwable instanceof Exception && throwable.getMessage()
                    .equals("cliente o cuenta inexistente"))
            .verify();
    }

    private Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setClienteId(1);
        cliente.setNombre("Anibal");
        cliente.setTelefono("1119999");
        cliente.setEstado(true);
        return cliente;
    }

    private Cuenta getCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(1000.0);
        cuenta.setClienteId(1L);
        cuenta.setNumeroCuenta(543211);
        cuenta.setEstado(true);
        return cuenta;
    }

    private Movimiento getMovimiento() throws ParseException {
        Movimiento movimiento = new Movimiento();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        movimiento.setId(1);
        movimiento.setTipoMovimiento("debito");
        movimiento.setSaldo(900.0);
        movimiento.setValor(100.0);
        movimiento.setFecha(sdf.parse("2023-01-06"));
        movimiento.setClienteId(1L);
        movimiento.setCuentaId(1L);
        return movimiento;
    }
}

