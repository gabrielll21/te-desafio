package com.sistema.desafios.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Subcategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    private String nome;

    @Size(max = 255)
    private String descricao;

    @ManyToOne(optional = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "subcategoria")
    private List<Desafio> desafios;
}
