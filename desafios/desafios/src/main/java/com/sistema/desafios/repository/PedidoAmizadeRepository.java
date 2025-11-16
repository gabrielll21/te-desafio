package com.sistema.desafios.repository;

import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.StatusPedidoAmizade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoAmizadeRepository extends JpaRepository<PedidoAmizade, Long> {
    
    // Buscar pedidos recebidos pendentes
    @Query("SELECT p FROM PedidoAmizade p WHERE p.destinatario.id = :usuarioId AND p.status = :status")
    List<PedidoAmizade> findPedidosRecebidosPendentes(@Param("usuarioId") Long usuarioId, @Param("status") StatusPedidoAmizade status);
    
    // Buscar pedidos enviados pendentes
    @Query("SELECT p FROM PedidoAmizade p WHERE p.solicitante.id = :usuarioId AND p.status = :status")
    List<PedidoAmizade> findPedidosEnviadosPendentes(@Param("usuarioId") Long usuarioId, @Param("status") StatusPedidoAmizade status);
    
    // Verificar se existe pedido pendente entre dois usuários (qualquer direção)
    @Query("SELECT p FROM PedidoAmizade p WHERE " +
           "((p.solicitante.id = :id1 AND p.destinatario.id = :id2) OR " +
           "(p.solicitante.id = :id2 AND p.destinatario.id = :id1)) " +
           "AND p.status = :status")
    Optional<PedidoAmizade> findPedidoPendenteEntre(@Param("id1") Long id1, @Param("id2") Long id2, @Param("status") StatusPedidoAmizade status);
}
