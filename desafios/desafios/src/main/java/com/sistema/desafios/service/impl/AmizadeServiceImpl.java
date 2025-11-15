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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AmizadeServiceImpl implements AmizadeService {

    @Autowired
    private PedidoAmizadeRepository pedidoAmizadeRepository;

    @Autowired
    private AmizadeRepository amizadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public PedidoAmizade criarPedido(Long idSolicitante, Long idDestinatario) {
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
            .orElseThrow(() -> new IllegalArgumentException("Usuário solicitante não encontrado"));
        Usuario destinatario = usuarioRepository.findById(idDestinatario)
            .orElseThrow(() -> new IllegalArgumentException("Usuário destinatário não encontrado"));

        if (solicitante.equals(destinatario)) {
            throw new IllegalArgumentException("Solicitante não pode ser o mesmo que o destinatário");
        }

        if (haPendenteEntre(solicitante.getId(), destinatario.getId())) {
            throw new IllegalArgumentException("Já existe um pedido pendente entre os usuários");
        }

        PedidoAmizade pedido = new PedidoAmizade();
        pedido.setSolicitante(solicitante);
        pedido.setDestinatario(destinatario);
        return pedidoAmizadeRepository.save(pedido);
    }
    @Override
    @Transactional
    public void aceitar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoAmizadeRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de amizade não encontrado"));
        
        if (!pedido.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Usuário atual não é o destinatário do pedido");
        }

        // Criar amizade se não existir
        if (!saoAmigos(pedido.getSolicitante().getId(), pedido.getDestinatario().getId())) {
            Amizade amizade = Amizade.of(pedido.getSolicitante(), pedido.getDestinatario());
            amizadeRepository.save(amizade);
        }

        // Atualizar status do pedido
        pedido.setStatus(StatusPedidoAmizade.ACEITO);
        pedidoAmizadeRepository.save(pedido);
    }

    @Override
    public void recusar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoAmizadeRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de amizade não encontrado"));
        
        if (!pedido.getDestinatario().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Usuário atual não é o destinatário do pedido");
        }

        // Atualizar status do pedido
        pedido.setStatus(StatusPedidoAmizade.RECUSADO);
        pedidoAmizadeRepository.save(pedido);
    }

    @Override
    public void cancelar(Long idPedido, Long idUsuarioAtual) {
        PedidoAmizade pedido = pedidoAmizadeRepository.findById(idPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido de amizade não encontrado"));
        
        if (!pedido.getSolicitante().getId().equals(idUsuarioAtual)) {
            throw new IllegalArgumentException("Usuário atual não é o solicitante do pedido");
        }

        // Atualizar status do pedido
        pedido.setStatus(StatusPedidoAmizade.CANCELADO);
        pedidoAmizadeRepository.save(pedido);
    }

    @Override
    public boolean saoAmigos(Long id1, Long id2) {
        return amizadeRepository.findByUsuarioAIdAndUsuarioBId(id1, id2).isPresent() ||
               amizadeRepository.findByUsuarioAIdAndUsuarioBId(id2, id1).isPresent();
    }
        // Implementar lógica para verificar se são amigos

    @Override
    public boolean haPendenteEntre(Long id1, Long id2) {
        return pedidoAmizadeRepository.findByParAAndParB(id1, id2).isPresent();
    }
        // Implementar lógica para verificar se há pedido pendente

    @Override
    public List<Usuario> listarMeusAmigos(Long meuId) {
        List<Amizade> amizades = amizadeRepository.findAllByUsuarioAOrUsuarioB(usuarioRepository.findById(meuId).orElseThrow(), usuarioRepository.findById(meuId).orElseThrow());
        return amizades.stream()
            .flatMap(amizade -> Stream.of(amizade.getUsuarioA(), amizade.getUsuarioB()))
            .distinct()
            .filter(usuario -> !usuario.getId().equals(meuId))
            .collect(Collectors.toList());
    }
        // Implementar lógica para listar amigos

    @Override
    public List<PedidoAmizade> listarRecebidos(Long meuId) {
        Usuario usuario = usuarioRepository.findById(meuId).orElseThrow();
        return pedidoAmizadeRepository.findAllByDestinatarioAndStatus(usuario, StatusPedidoAmizade.PENDENTE);
    }
        // Implementar lógica para listar pedidos recebidos

    @Override
    public List<PedidoAmizade> listarEnviados(Long meuId) {
        Usuario usuario = usuarioRepository.findById(meuId).orElseThrow();
        return pedidoAmizadeRepository.findAllBySolicitanteAndStatus(usuario, StatusPedidoAmizade.PENDENTE);
    }
        // Implementar lógica para listar pedidos enviados

}
