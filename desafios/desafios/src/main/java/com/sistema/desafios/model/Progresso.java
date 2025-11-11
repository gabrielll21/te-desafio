package com.sistema.desafios.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Progresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Desafio desafio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAcaoProgresso acao;

    @Column(length = 500)
    private String nota;

    @CreationTimestamp
    private Instant criadoEm;
}
