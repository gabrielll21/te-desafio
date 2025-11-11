package com.sistema.desafios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuarioA", "usuarioB"}))
public class Amizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Usuario usuarioA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Usuario usuarioB;

    public Usuario getUsuarioA() {
        return usuarioA;
}

    public Usuario getUsuarioB() {
        return usuarioB;
    }
    
    // Remover método duplicado
    // Este método não é necessário e deve ser removido

    @CreationTimestamp
    private Instant criadoEm;

    public static Amizade of(Usuario a, Usuario b) {
        Amizade amizade = new Amizade();
        amizade.usuarioA = a.getId() < b.getId() ? a : b;
        amizade.usuarioB = a.getId() < b.getId() ? b : a;
        return amizade;
    }

}