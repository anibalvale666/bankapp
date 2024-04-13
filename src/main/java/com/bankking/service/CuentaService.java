package com.bankking.service;

import com.bankking.models.Cuenta;

import java.util.List;

public interface CuentaService {
    public List<Cuenta> getCuentas();

    public Cuenta saveCuenta(Cuenta cuenta);

    public Cuenta updateCuenta(Long id, Cuenta cuenta);

    public Boolean deleteCuenta(Long id);
}
