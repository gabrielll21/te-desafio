package com.sistema.desafios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"parA", "parB"}))
public class PedidoAmizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Usuario solicitante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Usuario destinatario;

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusPedidoAmizade status;

    public StatusPedidoAmizade getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoAmizade status) {
        this.status = status;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    @NotNull
    private Long parA;

    @NotNull
    private Long parB;

    @CreationTimestamp
    private Instant criadoEm;

    @UpdateTimestamp
    private Instant atualizadoEm;

    private Instant respondidoEm;

    @PrePersist
    @PreUpdate
    private void validar() {
        if (solicitante.equals(destinatario)) {
            throw new IllegalArgumentException("Solicitante não pode ser o mesmo que o destinatário");
        }
        parA = Math.min(solicitante.getId(), destinatario.getId());
        parB = Math.max(solicitante.getId(), destinatario.getId());
        status = StatusPedidoAmizade.PENDENTE;
    }
}
