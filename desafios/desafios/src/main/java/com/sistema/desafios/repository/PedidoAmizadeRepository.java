package com.sistema.desafios.repository;

import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.model.StatusPedidoAmizade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoAmizadeRepository extends JpaRepository<PedidoAmizade, Long> {
    Optional<PedidoAmizade> findByParAAndParB(Long a, Long b);
    
    @Query("SELECT p FROM PedidoAmizade p JOIN FETCH p.destinatario JOIN FETCH p.solicitante WHERE p.destinatario = :usuario AND p.status = :status")
    List<PedidoAmizade> findAllByDestinatarioAndStatus(@Param("usuario") Usuario u, @Param("status") StatusPedidoAmizade s);
    
    @Query("SELECT p FROM PedidoAmizade p JOIN FETCH p.destinatario JOIN FETCH p.solicitante WHERE p.solicitante = :usuario AND p.status = :status")
    List<PedidoAmizade> findAllBySolicitanteAndStatus(@Param("usuario") Usuario u, @Param("status") StatusPedidoAmizade s);
}
