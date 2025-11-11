package com.sistema.desafios.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Desafio desafio;

    @Column(length = 80, nullable = false)
    private String acao;

    @Column(length = 500)
    private String descricao;

    @CreationTimestamp
    private Instant criadoEm;
}
