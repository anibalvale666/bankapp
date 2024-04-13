package com.bankking.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cliente")
@NoArgsConstructor
public class Cliente extends Persona{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PrimaryKeyJoinColumn
    private long clienteId;
    @Column
    private String contrasena;
    @Column
    private Boolean estado;

    // Constructor con parámetros
    public Cliente(String contrasena, Boolean estado,
                   String nombre, String genero, Integer edad,
                   String identificacion, String direccion, String telefono) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.contrasena = contrasena;
        this.estado = estado;
    }

}
