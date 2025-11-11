package com.sistema.desafios.repository;

import com.sistema.desafios.model.Amizade;
import com.sistema.desafios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AmizadeRepository extends JpaRepository<Amizade, Long> {
    Optional<Amizade> findByUsuarioAIdAndUsuarioBId(Long a, Long b);
    List<Amizade> findAllByUsuarioAOrUsuarioB(Usuario a, Usuario b);
}
