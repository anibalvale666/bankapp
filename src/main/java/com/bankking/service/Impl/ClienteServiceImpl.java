package com.bankking.service.Impl;

import com.bankking.models.Cliente;
import com.bankking.repository.ClienteRepository;
import com.bankking.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Flux<Cliente> getClientes() {
        return Flux.fromIterable(clienteRepository.findAll());
    }

    public Mono<Cliente> saveCliente(Cliente cliente){
        return Mono.just(clienteRepository.save(cliente));
    }

    public Mono<Cliente> updateCliente(Long id, Cliente cliente) {
        Cliente updateCliente =  clienteRepository.findById(id).get();
        updateCliente.setContrasena(cliente.getContrasena());
        updateCliente.setEstado(cliente.getEstado());
        updateCliente.setNombre(cliente.getNombre());
        updateCliente.setGenero(cliente.getGenero());
        updateCliente.setEdad(cliente.getEdad());
        updateCliente.setIdentificacion(cliente.getIdentificacion());
        updateCliente.setDireccion(cliente.getDireccion());
        updateCliente.setTelefono(cliente.getTelefono());
        return Mono.just(clienteRepository.save(updateCliente));

    }

    public Mono<Boolean> deleteCliente(Long id) {
        Optional<Cliente> deleteCliente =  clienteRepository.findById(id);

        if(deleteCliente.isPresent()) {
            clienteRepository.delete(deleteCliente.get());
            return Mono.just(true);
        }
        return Mono.just(false);

    }
}
