package com.bankking.service.Impl;

import com.bankking.models.Cuenta;
import com.bankking.repository.ClienteRepository;
import com.bankking.repository.CuentaRepository;
import com.bankking.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cuenta> getCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta saveCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta updateCuenta(Long id, Cuenta cuenta) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            Cuenta cuentaUpdate = cuentaOptional.get();
            cuentaUpdate.setEstado(cuenta.getEstado());
            cuentaUpdate.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaUpdate.setSaldoInicial(cuenta.getSaldoInicial());
            return cuentaRepository.save(cuentaUpdate);
        } else {
                  return null;
        }

    }

    @Override
    public Boolean deleteCuenta(Long id) {
        Cuenta toDelete = cuentaRepository.findById(id).get();
        cuentaRepository.delete(toDelete);
        return true;
    }
}
