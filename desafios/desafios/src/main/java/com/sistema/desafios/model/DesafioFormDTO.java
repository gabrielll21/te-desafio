package com.sistema.desafios.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;

@Data
public class DesafioFormDTO {
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
    private StatusDesafio status;

    @NotNull
    @Min(0)
    private Integer pontuacaoMaxima;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer dificuldade;

    @NotNull
    private Long categoriaId;

    private Long subcategoriaId; // opcional
}
