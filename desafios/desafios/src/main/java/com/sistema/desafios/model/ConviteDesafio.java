package com.sistema.desafios.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class ConviteDesafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario remetente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario destinatario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Desafio desafio;

    @Column(length = 255)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConviteDesafio status;

    @CreationTimestamp
    private Instant criadoEm;

    @UpdateTimestamp
    private Instant atualizadoEm;

    private Instant respondidoEm;

    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = StatusConviteDesafio.PENDENTE;
        }
        if (remetente != null && destinatario != null && remetente.equals(destinatario)) {
            throw new IllegalArgumentException("O remetente não pode ser igual ao destinatário.");
        }
    }
}
