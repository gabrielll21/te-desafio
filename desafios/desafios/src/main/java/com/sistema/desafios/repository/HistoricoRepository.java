package com.sistema.desafios.repository;

import com.sistema.desafios.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findAllByDesafioIdOrderByCriadoEmDesc(Long desafioId);
}
