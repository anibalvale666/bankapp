package com.bankking.controllers;

import com.bankking.models.Cuenta;
import com.bankking.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CuentaController {
    @Autowired
    private CuentaService service;

    @GetMapping("cuentas")
    public List<Cuenta> getCliente() {
        return service.getCuentas();
    }

    @PostMapping("cuentas")
    public Cuenta saveCliente(@RequestBody Cuenta cuenta) {
        return service.saveCuenta(cuenta);
    }

    @PutMapping("cuentas/{id}")
    public Cuenta updateCliente(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        return service.updateCuenta(id, cuenta);
    }

    @DeleteMapping("cuentas/{id}")
    public Boolean deleteCliente(@PathVariable Long id) {
        return service.deleteCuenta(id);
    }

}
