package com.bankking.controllers;



import com.bankking.models.Cliente;
import com.bankking.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("clientes")
    public ResponseEntity<Flux<Cliente>> getCliente() {
       return ResponseEntity.status(HttpStatus.OK)
                .body(service.getClientes());

    }

    @PostMapping("clientes")
    public ResponseEntity<Mono<Cliente>> saveCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(service.saveCliente(cliente));

    }

    @PutMapping("clientes/{id}")
    public ResponseEntity<Mono<Cliente>> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(service.updateCliente(id, cliente));
    }

    @DeleteMapping("clientes/{id}")
    public Mono<ResponseEntity<String>> deleteCliente(@PathVariable Long id) {
        return service.deleteCliente(id)
            .flatMap(deleted -> {
                if (deleted) {
                    return Mono.just(ResponseEntity.ok("Se eliminó correctamente"));
                } else {
                    return Mono.just(ResponseEntity.notFound().<String>build());
                }
            })
            .defaultIfEmpty(ResponseEntity.notFound().<String>build());
    }

}
