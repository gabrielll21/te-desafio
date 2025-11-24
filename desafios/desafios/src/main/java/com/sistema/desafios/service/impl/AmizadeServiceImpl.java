package com.sistema.desafios.service.impl;

import com.sistema.desafios.model.Amizade;
import com.sistema.desafios.model.PedidoAmizade;
import com.sistema.desafios.model.StatusPedidoAmizade;
import com.sistema.desafios.model.Usuario;
import com.sistema.desafios.repository.AmizadeRepository;
import com.sistema.desafios.repository.PedidoAmizadeRepository;
import com.sistema.desafios.repository.UsuarioRepository;
import com.sistema.desafios.service.AmizadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AmizadeServiceImpl implements AmizadeService {

    @Autowired
    private PedidoAmizadeRepository pedidoRepository;

    @Autowired
    private AmizadeRepository amizadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PedidoAmizade criarPedido(Long idSolicitante, Long idDestinatario) {
        // Validar IDs
        if (idSolicitante == null || idDestinatario == null) {
            throw new IllegalArgumentException("IDs não podem ser nulos");
        }
        
        if (idSolicitante.equals(idDestinatario)) {
            throw new IllegalArgumentException("Você não pode enviar pedido para si mesmo");
        }

        // Buscar usuários
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
            .orElseThrow(() -> new IllegalArgumentException("Usuário solicitante não encontrado"));
        
        Usuario destinatario = usuarioRepository.findById(idDestinatario)
            .orElseThrow(() -> new IllegalArgumentException("Usuário destinatário não encontrado"));

        // Verificar se já são amigos
        if (saoAmigos(idSolicitante, idDestinatario)) {
            throw new IllegalArgumentException("Vocês já são amigos!");
        }

        // Verificar se já existe pedido pendente
        Optional<PedidoAmizade> pedidoExistente = pedidoRepository.findPedidoPendenteEntre(
            idSolicitante, 
            idDestinatario, 
            StatusPedidoAmizade.PENDENTE
        );
        
        if (pedidoExistente.isPresent()) {
            throw new IllegalArgumentException("Já existe um pedido pendente entre vocês");
        }

        // Criar novo pedido
        PedidoAmizade pedido = new PedidoAmizade(solicitante, destinatario);
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void aceitar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        if (!pedido.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Você não pode aceitar este pedido");
        }

        if (pedido.getStatus() != StatusPedidoAmizade.PENDENTE) {
            throw new IllegalArgumentException("Este pedido já foi processado");
        }

        // Criar amizade se não existir
        Long id1 = pedido.getSolicitante().getId();
        Long id2 = pedido.getDestinatario().getId();
        
        if (!saoAmigos(id1, id2)) {
            Usuario usuarioA = pedido.getSolicitante();
            Usuario usuarioB = pedido.getDestinatario();
            Amizade amizade = Amizade.of(usuarioA, usuarioB);
            amizadeRepository.save(amizade);
        }

        // Atualizar status do pedido
        pedido.setStatus(StatusPedidoAmizade.ACEITO);
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void recusar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        if (!pedido.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Você não pode recusar este pedido");
        }

        if (pedido.getStatus() != StatusPedidoAmizade.PENDENTE) {
            throw new IllegalArgumentException("Este pedido já foi processado");
        }

        pedido.setStatus(StatusPedidoAmizade.RECUSADO);
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void cancelar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        if (!pedido.getSolicitante().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Você não pode cancelar este pedido");
        }

        if (pedido.getStatus() != StatusPedidoAmizade.PENDENTE) {
            throw new IllegalArgumentException("Este pedido já foi processado");
        }

        pedido.setStatus(StatusPedidoAmizade.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Override
    public boolean saoAmigos(Long id1, Long id2) {
        if (id1 == null || id2 == null || id1.equals(id2)) {
            return false;
        }
        return amizadeRepository.findByUsuarioAIdAndUsuarioBId(id1, id2).isPresent() ||
               amizadeRepository.findByUsuarioAIdAndUsuarioBId(id2, id1).isPresent();
    }

    @Override
    public boolean haPendenteEntre(Long id1, Long id2) {
        if (id1 == null || id2 == null || id1.equals(id2)) {
            return false;
        }
        return pedidoRepository.findPedidoPendenteEntre(id1, id2, StatusPedidoAmizade.PENDENTE).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarMeusAmigos(Long meuId) {
        if (meuId == null) {
            return List.of();
        }
        
        // Usar query com JOIN FETCH para carregar os usuários junto com as amizades
        List<Amizade> amizades = amizadeRepository.findAllByUsuarioIdWithUsers(meuId);
        
        return amizades.stream()
            .flatMap(amizade -> Stream.of(amizade.getUsuarioA(), amizade.getUsuarioB()))
            .distinct()
            .filter(u -> !u.getId().equals(meuId))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoAmizade> listarRecebidos(Long meuId) {
        if (meuId == null) {
            return List.of();
        }
        return pedidoRepository.findPedidosRecebidosPendentes(meuId, StatusPedidoAmizade.PENDENTE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoAmizade> listarEnviados(Long meuId) {
        if (meuId == null) {
            return List.of();
        }
        return pedidoRepository.findPedidosEnviadosPendentes(meuId, StatusPedidoAmizade.PENDENTE);
    }
}
