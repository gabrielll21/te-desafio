package com.sistema.desafios.repository;

import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.model.StatusPedidoAmizade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoAmizadeRepository extends JpaRepository<PedidoAmizade, Long> {
    Optional<PedidoAmizade> findByParAAndParB(Long a, Long b);
    List<PedidoAmizade> findAllByDestinatarioAndStatus(Usuario u, StatusPedidoAmizade s);
    List<PedidoAmizade> findAllBySolicitanteAndStatus(Usuario u, StatusPedidoAmizade s);
}
