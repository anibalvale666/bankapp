package com.bankking.service;


import com.bankking.models.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteService {
    public Flux<Cliente> getClientes();

    public Mono<Cliente> saveCliente(Cliente cliente);

    public Mono<Cliente> updateCliente(Long id, Cliente cliente);

    public Mono<Boolean> deleteCliente(Long id);
}
