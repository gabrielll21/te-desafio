package com.sistema.desafios.repository;

import com.sistema.desafios.model.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {
    List<Progresso> findAllByDesafioIdOrderByCriadoEmAsc(Long desafioId);
    List<Progresso> findAllByUsuarioIdAndDesafioIdOrderByCriadoEmAsc(Long usuarioId, Long desafioId);
    long countByUsuarioId(Long usuarioId);
}
