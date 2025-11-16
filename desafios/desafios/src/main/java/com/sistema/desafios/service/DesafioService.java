package com.sistema.desafios.service;

import com.sistema.desafios.model.Desafio;
import com.sistema.desafios.model.DesafioFormDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DesafioService {
    Optional<Desafio> findById(Long id);
    List<Desafio> findAll();
    Page<Desafio> listar(String q, Long categoriaId, int page, int size);
    List<Desafio> listarMeusDesafios(Long idCriador);
    Desafio criar(DesafioFormDTO dto, Long idCriador);
    Desafio atualizar(Long id, DesafioFormDTO dto, Long idCriador);
    void excluir(Long id, Long idCriador);
    Desafio detalhar(Long id);
}
