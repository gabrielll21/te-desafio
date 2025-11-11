package com.sistema.desafios.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class Desafio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String titulo;

    @NotBlank
    @Size(max = 2000)
    private String descricao;

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataFinal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusDesafio status;

    @NotNull
    @Min(0)
    private Integer pontuacaoMaxima;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer dificuldade;

    @ManyToOne(optional = false)
    private Categoria categoria;

    @ManyToOne(optional = true)
    private Subcategoria subcategoria; // opcional

    @ManyToOne(optional = false)
    private Usuario criador;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
