package com.sistema.desafios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "pedido_amizade")
public class PedidoAmizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    @NotNull
    private Usuario solicitante;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    @NotNull
    private Usuario destinatario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedidoAmizade status = StatusPedidoAmizade.PENDENTE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    // Campos para compatibilidade com estrutura antiga do banco
    @NotNull
    @Column(name = "parA", nullable = false)
    private Long parA;

    @NotNull
    @Column(name = "parB", nullable = false)
    private Long parB;

    @PrePersist
    @PreUpdate
    private void calcularParAeParB() {
        if (solicitante != null && destinatario != null) {
            this.parA = Math.min(solicitante.getId(), destinatario.getId());
            this.parB = Math.max(solicitante.getId(), destinatario.getId());
        }
    }

    // Construtores
    public PedidoAmizade() {
    }

    public PedidoAmizade(Usuario solicitante, Usuario destinatario) {
        if (solicitante == null || destinatario == null) {
            throw new IllegalArgumentException("Solicitante e destinatário não podem ser nulos");
        }
        if (solicitante.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("Solicitante não pode ser o mesmo que o destinatário");
        }
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.status = StatusPedidoAmizade.PENDENTE;
        // Calcular parA e parB imediatamente
        calcularParAeParB();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public StatusPedidoAmizade getStatus() {
        return status;
    }

    public void setStatus(StatusPedidoAmizade status) {
        this.status = status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }
}
