package com.sistema.desafios.repository;

import com.sistema.desafios.model.Amizade;
import com.sistema.desafios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AmizadeRepository extends JpaRepository<Amizade, Long> {
    Optional<Amizade> findByUsuarioAIdAndUsuarioBId(Long a, Long b);
    List<Amizade> findAllByUsuarioAOrUsuarioB(Usuario a, Usuario b);
    
    @Query("SELECT DISTINCT a FROM Amizade a " +
           "JOIN FETCH a.usuarioA " +
           "JOIN FETCH a.usuarioB " +
           "WHERE a.usuarioA.id = :usuarioId OR a.usuarioB.id = :usuarioId")
    List<Amizade> findAllByUsuarioIdWithUsers(@Param("usuarioId") Long usuarioId);
}
