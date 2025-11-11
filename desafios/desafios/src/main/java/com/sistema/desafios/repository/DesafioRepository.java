package com.sistema.desafios.repository;

import com.sistema.desafios.model.Desafio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesafioRepository extends JpaRepository<Desafio, Long> {
    Page<Desafio> findByTituloContainingIgnoreCase(String q, Pageable p);
    Page<Desafio> findByCategoriaId(Long categoriaId, Pageable p);
    Page<Desafio> findByTituloContainingIgnoreCaseAndCategoriaId(String q, Long categoriaId, Pageable p);
}
